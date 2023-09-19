#!/bin/bash

# number of times the script will be executed.
n=${1:-10}

# removing old files
rm -f time.txt
rm -f visited_methods.txt
rm -f conflicts_log.txt
rm -f out.txt
rm -f outConsole.txt
rm -f output/data/soot-results.csv
rm -f output/data/results.pdf
rm -r output/results
mkdir -p output/results

## loop to execute the script gradlew run n times (executing the analyses)
for ((i=1;i<=$n;i++))
do
    folder_name="execution-$i"
    mkdir $folder_name

    # execute the script gradlew run (executing the analyses)
    ./gradlew run -DmainClass="services.outputProcessors.soot.Main" --args="-cd -cde -cf -icf -dfp -idfp -oa -ioa -pdg -pdge -report -t 0"

    # move the files generated by the script to the current execution folder
    mv outConsole.txt time.txt confusion_matrix.jpg visited_methods.txt conflicts_log.txt output/data/soot-results.csv output/data/results.pdf $folder_name
done

# moving the results by execution for output/results
mv -f execution-* output/results
find . -name "results_*" -type f -delete
find . -name "resultTime*" -type f -delete

# generating n CSV files with the times from the execution logs
python3 scripts/experiment_static_analysis/generate_time_csv_from_logs.py $n
python3 scripts/experiment_static_analysis/generate_visited_methods_csv_from_logs.py $n
python3 scripts/experiment_static_analysis/generate_conflicts_csv_from_logs.py $n

# calculating and generating results with the created times
# generate a sheet for each execution and summarize the results generating the graphs
python3 scripts/experiment_static_analysis/summarize_time_results.py $n

# moving the results for output/results
mv rain_cloud_analysis_and_configuration_time.jpg output/results
mv rain_cloud_scenarios_time.jpg output/results
mv rain_cloud_analysis_time.jpg output/results
mv rain_cloud_experiment_time.jpg output/results

mv rain_cloud_scenarios_time.pdf output/results
mv rain_cloud_analysis_time.pdf output/results
mv rain_cloud_analysis_and_configuration_time.pdf output/results
mv rain_cloud_experiment_time.pdf output/results

# moving timing results to output/results/times
mkdir -p output/results/times
mv resultTime* output/results/times
mkdir -p output/results/visitedMethods
mv visited_methods* output/results/visitedMethods
mkdir -p output/results/conflictsLogs
mv conflicts_log* output/results/conflictsLogs

# moving result by scenario and by execution generated to output/results
mkdir -p output/results/sheets
mv results_by_scenario_execution_* output/results/sheets
mv results_by_scenario_all_execution.csv output/results/

# checking if there is a difference between the results (FP, FN, TP and TN) of the executions
python3 scripts/experiment_static_analysis/check_diff_results_pdf.py $n
mv diff_files.pdf output/results/
