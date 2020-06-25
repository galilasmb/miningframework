# This script receives as input the path to a framework input file, the path to a directory generated by the miningframework and a github acess token, it downloads the release files from github and moves the files to the directory passed as input.


import sys
import requests
import json
import subprocess
import time
import shutil
from os import path, listdir, remove, makedirs
import csv

PATH = "path"
NAME = "name"
FORK_URL = "fork_url"

BRANCH = "branch"
STATE = "state"

PROJECT = "project"
MERGE_COMMIT = "merge commit"
RESULT = "result"
GITHUB_API= "https://api.github.com"
TRAVIS_API = "https://api.travis-ci.org"
LOGIN = "login"
BROWSER_DOWNLOAD_URL='browser_download_url'
ASSETS="assets"
MESSAGE_PREFIX="Trigger build #"
RELEASE_PREFIX= "fetchjar-"
FINISHED = "finished"
UNTAGGED = "untagged"
ORIGINAL_WITHOUT_DEPENDENCIES_MERGE_FOLDER = "original-without-dependencies/merge"
input_path = sys.argv[1] # input path passed as cli argument
output_path = sys.argv[2].rstrip("/") # output path passed as cli argument
token = sys.argv[3] # token passed as cli argument

def fetch_jars(input_path, output_path, token):
    # this method reads a csv input file, with the projects name and path
    # for each project it downloads the build generated via github releases
    # and moves the builds to the output generated by the framework
    print("Starting build collection")

    try:
        token_user_name = get_github_user(token)[LOGIN]

        parsed_input = read_csv(input_path, ",")
        parsed_output = read_csv(output_path + "/data/results.csv", ";")
        
        projects = map(lambda p: process_project(p, token_user_name), parsed_input)

        new_results_file = []

        for project in projects:
            github_project = project[FORK_URL]
            project_name = project[NAME]

            project_scenarios = filter(lambda scenario: scenario[PROJECT] == project_name, parsed_output)
            try:
                print (project_name)        

                get_builds_and_wait(github_project)

                releases = get_github_releases(token, github_project)
                build_map = mount_github_releases_hash(releases)

                for scenario in project_scenarios:
                    has_build = False
                    commit_sha = scenario[MERGE_COMMIT]
                    build_path = mount_build_path(project_name, commit_sha)
                    
                    print(commit_sha)
                    if (path.exists(build_path)):
                        has_build = True
                    elif (commit_sha in build_map):
                        try:
                            download_build(project_name, commit_sha, build_map[commit_sha])
                                
                            has_build = True
                        except Exception as e:
                            print ("Error downloading scenario: " + str(e))

                    scenario.update({ "has_build": "true" if has_build else "false" })
                    new_results_file.append(scenario)

            except Exception as e:
                print ("Error fetching builds for project " + project_name + ": " + str(e))
        
        print (new_results_file)
        save_results_with_builds(output_path, new_results_file)
    except Exception as e:
        print (e)

def mount_github_releases_hash(releases):
    result_hash = {}
    for release in  releases:
        if (release[NAME].startswith(RELEASE_PREFIX)):
            commit_sha = strip_release_prefix(release)
            result_hash[commit_sha] = release
    return result_hash

def process_project(data, token_user_name):
    project = {}
    
    splited_project_path = data[PATH].split('/')
    github_project_name = splited_project_path[len(splited_project_path) - 1]
    github_project = token_user_name + '/' + github_project_name
    # check if framework used optional custom name

    project[PATH] = data[PATH]
    project[FORK_URL] = github_project
    project[NAME] = data[NAME] if data[NAME] else github_project_name

    return project

def download_build(project_name, commit_sha, release):
    scenario_path = mount_scenario_path(project_name, commit_sha)

    print(fetch_jars)
    
    print ("Downloading")
    if path.exists(scenario_path):
        tar_path = scenario_path + "result.tar.gz"
        
        build_path = mount_build_path(project_name, commit_sha)

        if path.exists(tar_path):
            remove(tar_path)
        download_url = get_download_url(release)

        download_file(download_url, tar_path)

        untar_and_remove_file(tar_path, build_path)
    else:
        raise Exception("Scenario folder: " + scenario_path + " doesn't exist")

def mount_build_path(project_name, commit_sha):
    return mount_scenario_path(project_name, commit_sha) + ORIGINAL_WITHOUT_DEPENDENCIES_MERGE_FOLDER

def mount_scenario_path(project_name, commit_sha):
    # mount path where the downloaded build will be moved to
    return output_path + '/files/' + project_name + '/' + commit_sha + "/" 

def strip_release_prefix(release):
    return release[NAME].replace(RELEASE_PREFIX, "")

def get_download_url(release):
    return release[ASSETS][0][BROWSER_DOWNLOAD_URL]

def save_results_with_builds(output_path, new_results_file):
    with open(output_path + "/data/results-with-build-information.csv", 'w') as outputFile:
        csv_writer = csv.DictWriter(outputFile, delimiter=";", 
            fieldnames=["project","merge commit","className","method","left modifications","has_build","left deletions","right modifications","right deletions"])

        csv_writer.writeheader()
        for scenario in new_results_file:
            csv_writer.writerow(scenario)


def read_csv(file_path, delimiter):
    with open(file_path, "r") as input_lines:
        return list(csv.DictReader(input_lines, delimiter=delimiter))

def download_file(url, target_path):
    # download file from url
    response = requests.get(url, stream=True)
    if response.status_code == 200:
        with open(target_path, 'wb') as f:
            f.write(response.raw.read())
    else:
        raise Exception("Download request returned status code: " + response.status_code)

def untar_and_remove_file(tar_path, output_path):
    makedirs(output_path)
    subprocess.call(['mkdir', output_path])
    subprocess.call(['tar', '-xf', tar_path, '-C', output_path ])
    subprocess.call(['rm', tar_path])

def get_builds_and_wait(project):
    has_pendent = True
    filtered_builds = []
    while (has_pendent):
        builds = get_travis_project_builds(project)
        filtered_builds = filter (lambda x: not x[BRANCH].startswith(UNTAGGED), builds)
        
        has_pendent = False
        for build in filtered_builds:
            has_pendent = has_pendent or (build[STATE] != FINISHED)
    
        if (has_pendent):
            print ("Waiting 30 seconds")
            time.sleep(30)
        else:
            for build in filtered_builds:
                print (build[BRANCH] + ": " + build[STATE] )


    return filtered_builds


def get_travis_project_builds(project):
    res = requests.get(TRAVIS_API + '/repos/' + project + '/builds')

    try: 
        res.raise_for_status()

        return res.json()
    except Exception as e:
        raise Exception("Error getting travis builds: " + str(e))
    
def get_github_user(token):
    res = requests.get(GITHUB_API + '/user', headers=get_headers(token))
    try:
        res.raise_for_status()

        return res.json()
    except Exception as e:
        raise Exception("Error getting github user: " + str(e))    


def get_github_releases(token, project):
    page = 1
    reqRes = get_github_releases_page(token, project, page)
    result = reqRes
    # this is a workaround to get all releases at once, it is needed because of the API pagination
    while len(reqRes):
        page += 1
        reqRes = get_github_releases_page(token, project, page)
        result = result + reqRes
    return result

def get_github_releases_page(token, project, page_number):
    res = requests.get(GITHUB_API + '/repos/' + project + '/releases?page=' + str(page_number),headers=get_headers(token))
    try:
        res.raise_for_status()

        return res.json()
    except Exception as e:
        raise Exception("Error getting github releases: " + str(e))


def get_headers(token):
    return {
        "Authorization": "token " + token
    }

def remove_commit_files_without_builds (output_path, project_name):
    files_path = output_path + "/files/" + project_name +  "/"

    if (path.exists(files_path)): 
        commit_dirs = listdir(files_path)

        for directory in commit_dirs:
            commit_dir = mount_scenario_path(project_name, directory)
            build_dir = mount_build_path(project_name, directory)

            if (not path.exists(build_dir)):
                shutil.rmtree(commit_dir)

        if (len (listdir(files_path)) == 0 ):
            shutil.rmtree(files_path)

fetch_jars(input_path, output_path, token)