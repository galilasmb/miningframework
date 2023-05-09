#!/bin/bash

# number of times the script will be executed.
n=${1:-10}

# removing old files
rm -f time.txt
rm -f out.txt
rm -f outConsole.txt
rm -f output/data/soot-results.csv
rm -f output/data/results.pdf
rm -r output/results
mkdir -p output/results

# loop to execute the script gradlew run n times
for ((i=1;i<=$n;i++))
do
    folder_name="execution-$i"
    mkdir $folder_name

    # execute the script gradlew run
    ./gradlew run -DmainClass="services.outputProcessors.soot.Main" --args="-icf -ioa -idfp -pdg -report -t 0"

    # move the files generated by the script to the current execution folder
    mv outConsole.txt time.txt output/data/soot-results.csv output/data/results.pdf $folder_name
done

# moving the results by execution for output/results
mv -f execution-* output/results
find . -name "results_*" -type f -delete
find . -name "resultTime*" -type f -delete

# generating a CSV file with the times from the logs
python3 scripts/experiment_static_analysis/generate_time_csv_from_logs.py $n

# calculating and generating results with the created times
python3 scripts/experiment_static_analysis/summarize_time_results.py $n

# moving the results for output/results
mv results_by_analysis.jpg output/results
mv results_by_scenarios.jpg output/results
mv results_analysis.pdf output/results
mv results_by_only_analysis.jpg output/results
mv results_only_analysis.pdf output/results
mv results_scenarios.pdf output/results
mv results_by_execution.jpg output/results
mv results_execution.pdf output/results

# moving result time generated for output/results/times
mkdir -p output/results/times
mv resultTime* output/results/times

# moving result by scenario and by execution generated for output/results
mkdir -p output/results/sheets
mv results_by_scenario_execution_* output/results/sheets
mv results_by_scenario_all_execution.csv output/results/

# checking if there is a difference between the results of the executions
python3 scripts/experiment_static_analysis/check_diff_results_pdf.py $n
mv diff_files.pdf output/results/
