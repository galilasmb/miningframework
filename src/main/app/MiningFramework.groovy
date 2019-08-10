package main.app

@Grab('com.google.inject:guice:4.2.2')
import com.google.inject.*
import java.io.File
import java.util.ArrayList
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

import main.arguments.*
import main.project.*
import main.interfaces.*
import main.util.*
import main.exception.InvalidArgsException
import main.exception.UnstagedChangesException
import main.exception.UnexpectedPostScriptException
import main.exception.NoAccessKeyException

class MiningFramework {

    private ArrayList<Project> projectList
   
    private StatisticsCollector statCollector
    private ExperimentalDataCollector dataCollector
    private CommitFilter commitFilter
    private ProjectProcessor projectProcessor
    private OutputProcessor outputProcessor

    static public Arguments arguments
    private final String LOCAL_PROJECT_PATH = 'localProject'
    private final String LOCAL_RESULTS_REPOSITORY_PATH = System.getProperty('user.home')
    
    @Inject
    public MiningFramework(ExperimentalDataCollector dataCollector, StatisticsCollector statCollector, CommitFilter commitFilter, ProjectProcessor projectProcessor, OutputProcessor outputProcessor) {
        this.dataCollector = dataCollector
        this.statCollector = statCollector
        this.commitFilter = commitFilter
        this.projectProcessor = projectProcessor
        this.outputProcessor = outputProcessor
    }

    static main(args) {
        ArgsParser argsParser = new ArgsParser()
        try {
            Arguments appArguments = argsParser.parse(args)
            
            if (appArguments.isHelp()) {
                argsParser.printHelp()
            } else {
                Class injectorClass = appArguments.getInjector()
                Injector injector = Guice.createInjector(injectorClass.newInstance())
                MiningFramework framework = injector.getInstance(MiningFramework.class)

                framework.setArguments(appArguments)

                FileManager.createOutputFiles(appArguments.getOutputPath(), appArguments.isPushCommandActive())
            
                printStartAnalysis()                

                String inputPath = arguments.getInputPath()
                
                ArrayList<Project> projectList = InputParser.getProjectList(inputPath)

                framework.setProjectList(projectList)
                framework.start()

                printFinishAnalysis()

                runPostScript()
            }
    
        } catch (InvalidArgsException e) {
            println e.message
            println 'Run the miningframework with --help to see the possible arguments'
        } catch (UnstagedChangesException | UnexpectedPostScriptException | NoAccessKeyException e) {
            println e.message
        }
    }

    public void start() {
        projectList = processProjects(projectList)

        BlockingQueue<Project> projectQueue = populateProjectsQueue(projectList)
        
        Thread [] workers = createAndStartMiningWorkers (projectQueue)

        waitForMiningWorkers(workers)

        processOutput()
    }

    BlockingQueue<Project> populateProjectsQueue(List<Project> projectList) {
        BlockingQueue<Project> projectQueue = new LinkedBlockingQueue<Project>()
        
        for (Project project : projectList ) {    
            projectQueue.add(project)
        }
        
        return projectQueue
    }

    Thread [] createAndStartMiningWorkers (BlockingQueue<Project> projectQueue) {
        int numOfThreads = arguments.getNumOfThreads()
        
        Thread [] workers = new Thread[numOfThreads]
        
        for (int i = 0; i < numOfThreads; i++) {
            String workerPath = "${LOCAL_PROJECT_PATH}/worker${i}" 
            Runnable worker = new MiningWorker(dataCollector, statCollector, commitFilter, projectQueue, workerPath);
            workers[i] = new Thread(worker)
            workers[i].start();
        }

        return workers
    }

    void waitForMiningWorkers (Thread[] workers) {
        for (int i = 0; i < workers.length ; i++) {
            workers[i].join();
        }
    }

    static private void runPostScript() {
        String postScript = arguments.getPostScript()
        if (postScript.length() > 0) {
            println "Executing post script..."
            try {
                String scriptOutput = ProcessRunner.runProcess(".", postScript.split(' ')).getText()
                println scriptOutput
            } catch (IOException e) {
                throw new UnexpectedPostScriptException(e.message)
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new UnexpectedPostScriptException(e.message)
            }
        }
    }

    private ArrayList<Project> processProjects(ArrayList<Project> projects) {
        return projectProcessor.processProjects(projects)
    }

    private void processOutput() {
        outputProcessor.processOutput()
    }

    public void setProjectList(ArrayList<Project> projectList) {
        this.projectList = projectList
    }

    void setArguments(Arguments arguments) {
        this.arguments = arguments
    }


    static void printStartAnalysis() {
        println "#### MINING STARTED ####\n"
    }

    static void printFinishAnalysis() {
        println "#### MINING FINISHED ####"
    }

}