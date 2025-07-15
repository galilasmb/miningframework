package services.outputProcessors.soot.arguments

import groovy.cli.commons.CliBuilder
import groovy.cli.commons.OptionAccessor

class ArgsParser {

    private CliBuilder cli
    private OptionAccessor options

    ArgsParser() {
        this.cli = new CliBuilder(usage: "./gradlew run -DmainClass=\"services.outputProcessors.soot.Main --args=\"[options]\"",
                header: "Options: ")

        defParameters()
    }

    private defParameters() {
        this.cli.h(longOpt: 'help', 'Show help for executing commands')
        this.cli.a(longOpt: 'allanalysis', 'Excute all analysis')
        this.cli.t(longOpt: 'timeout', args: 1, argName: 'timeout', "timeout (default: 240)")
        this.cli.df(longOpt: 'svfa-intraprocedural',  "Run svfa-intraprocedural")
        this.cli.idf(longOpt: 'svfa-interprocedural',  "Run svfa-interprocedural")
        this.cli.cf(longOpt: 'dfp-confluence-intraprocedural',  "Run dfp-confluence-intraprocedural")
        this.cli.icf(longOpt: 'dfp-confluence-interprocedural',  "Run dfp-confluence-interprocedural")
        this.cli.cfpa(longOpt: 'dfp-confluence-intraprocedural-pa',  "Run dfp-confluence-intraprocedural with SPARK")
        this.cli.icfpa(longOpt: 'dfp-confluence-interprocedural-pa',  "Run dfp-confluence-interprocedural with SPARK")
        this.cli.oa(longOpt: 'overriding-intraprocedural',  "Run overriding-intraprocedural")
        this.cli.ioa(longOpt: 'overriding-interprocedural',  "Run overriding-interprocedural")
        this.cli.oapa(longOpt: 'oa-with-pa',  "Run overriding-intraprocedural with SPARK")
        this.cli.ioapa(longOpt: 'ioa-with-pa',  "Run overriding-interprocedural with SPARK")
        this.cli.dfp(longOpt: 'dfp-intra',  "Run dfp-intra")
        this.cli.idfp(longOpt: 'dfp-inter',  "Run dfp-inter")
        this.cli.dfppa(longOpt: 'dfp-intra-pa',  "Run dfp-intra with SPARK")
        this.cli.idfppa(longOpt: 'dfp-inter-pa',  "Run dfp-inter with SPARK")
        this.cli.cd(longOpt: 'cd',  "Run cd")
        this.cli.cde(longOpt: 'cde',  "Run cd-e")
        this.cli.pdg(longOpt: 'pdg',  "Run pdg")
        this.cli.pdge(longOpt: 'pdge',  "Run pdg-e")
        this.cli.cdpa(longOpt: 'cd-pa',  "Run cd with SPARK")
        this.cli.cdepa(longOpt: 'cde-pa',  "Run cd-e with SPARK")
        this.cli.pdgpa(longOpt: 'pdg-pa',  "Run pdg with SPARK")
        this.cli.pdgepa(longOpt: 'pdge',  "Run pdg-e with SPARK")
        this.cli.pd(longOpt: 'pessimistic-dataflow',  "Run pessimistic-dataflow")
        this.cli.report(longOpt: 'report',  "Run report results for experiment using -icf -ioa -idfp -pdg")
        this.cli.r(longOpt: 'reachability',  "Run reachability")
    }

    Arguments parse(args) {
        this.options = this.cli.parse(args)
        Arguments resultArgs = new Arguments()

        parseOptions(resultArgs)

        return resultArgs
    }

    void printHelp() {
        this.cli.usage()
    }


    private void parseOptions(Arguments args) {
        if (!this.options.a) {
            args.setAllanalysis(false)
        }
        if (this.options.h) {
            args.setIsHelp(true)
        }
        if (this.options.t) {
            args.setTimeout(this.options.t.toLong());
        }
        if (this.options.df) {
            args.setDfIntra(true)
        }
        if (this.options.idf) {
            args.setDfInter(true)
        }
        if (this.options.cf) {
            args.setCfIntra(true)
        }
        if (this.options.icf) {
            args.setCfInter(true)
        }
        if (this.options.cfpa) {
            args.setCfIntraPA(true)
        }
        if (this.options.icfpa) {
            args.setCfInterPA(true)
        }
        if (this.options.oa) {
            args.setOaIntra(true)
        }
        if (this.options.ioa) {
            args.setOaInter(true)
        }
        if (this.options.ioapa) {
            args.setOaInterPA(true)
        }
        if (this.options.oapa) {
            args.setOaIntraPA(true)
        }
        if (this.options.dfp) {
            args.setDfpIntra(true)
        }
        if (this.options.idfp) {
            args.setDfpInter(true)
        }
        if (this.options.dfppa) {
            args.setDfpIntraPA(true)
        }
        if (this.options.idfppa) {
            args.setDfpInterPA(true)
        }
        if (this.options.cd) {
            args.setCd(true)
        }
        if (this.options.cde) {
            args.setCde(true)
        }
        if (this.options.pdg) {
            args.setPdg(true)
        }
        if (this.options.pdge) {
            args.setPdge(true)
        }
        if (this.options.cdpa) {
            args.setCdPA(true)
        }
        if (this.options.cdepa) {
            args.setCdePA(true)
        }
        if (this.options.pdgpa) {
            args.setPdgPA(true)
        }
        if (this.options.pdgepa) {
            args.setPdgePA(true)
        }
        if (this.options.pd) {
            args.setPessimisticDataflow(true)
        }
        if (this.options.report) {
            args.setReport(true)
        }
        if (this.options.r) {
            args.setReachability(true)
        }
    }
}
