package services.outputProcessors.soot.arguments

class Arguments {
    private boolean isHelp
    private boolean allanalysis
    private boolean dfIntra
    private boolean dfInter
    private boolean cfIntra
    private boolean cfInter
    private boolean cfIntraPA
    private boolean cfInterPA
    private boolean oaIntra
    private boolean oaInter
    private boolean oaIntraPA
    private boolean oaInterPA
    private boolean dfpIntra
    private boolean dfpInter
    private boolean dfpIntraPA
    private boolean dfpInterPA
    private boolean cd
    private boolean cde
    private boolean pdg
    private boolean pdge
    private boolean cdPA
    private boolean cdePA
    private boolean pdgPA
    private boolean pdgePA
    private boolean pessimisticDataflow
    private boolean reachability
    private boolean report
    private long timeout
    private long depthLimit
    private boolean printDepthSVFA

    Arguments() { // set the default values for all parameters
        isHelp = false
        allanalysis = true
        dfIntra = false
        dfInter = false
        cfIntra = false
        cfInter = false
        dfpIntraPA = false
        dfpInterPA = false
        cfIntraPA = false
        cfInterPA = false
        oaIntra = false
        oaInter = false
        oaIntraPA = false
        oaInterPA = false
        dfpIntra = false
        dfpInter = false
        cd = false
        cde = false
        pdg = false
        pdge = false
        cdPA = false
        cdePA = false
        pdgPA = false
        pdgePA = false
        pessimisticDataflow = false
        reachability = false
        report = false
        timeout = 240
        printDepthSVFA = false
        depthLimit = 5
    }

    void setCfIntraPA(boolean cfIntraPA) {
        this.cfIntraPA = cfIntraPA
    }

    void setCfInterPA(boolean cfInterPA) {
        this.cfInterPA = cfInterPA
    }

    void setDfpIntraPA(boolean dfpIntraPA) {
        this.dfpIntraPA = dfpIntraPA
    }

    void setDfpInterPA(boolean dfpInterPA) {
        this.dfpInterPA = dfpInterPA
    }

    boolean getDfpIntraPA() {
        return dfpIntraPA
    }

    boolean getCfIntraPA() {
        return cfIntraPA
    }

    boolean getCfInterPA() {
        return cfInterPA
    }

    boolean getDfpInterPA() {
        return dfpInterPA
    }

    boolean getReport() {
        return report
    }

    void setDepthLimit(long depthLimit) {
        this.depthLimit = depthLimit
    }

    void setPrintDepthSVFA(boolean printDepthSVFA) {
        this.printDepthSVFA = printDepthSVFA
    }

    long getDepthLimit() {
        return depthLimit
    }

    boolean getPrintDepthSVFA() {
        return printDepthSVFA
    }
    boolean isHelp() {
        return isHelp
    }

    void setIsHelp(boolean isHelp) {
        this.isHelp = isHelp
    }

    boolean getAllanalysis() {
        return allanalysis
    }

    void setAllanalysis(boolean allanalysis) {
        this.allanalysis = allanalysis
    }

    boolean getDfIntra() {
        return dfIntra
    }

    void setDfIntra(boolean dfIntra) {
        this.dfIntra = dfIntra
    }

    boolean getDfInter() {
        return dfInter
    }

    void setDfInter(boolean dfInter) {
        this.dfInter = dfInter
    }

    boolean getCfIntra() {
        return cfIntra
    }

    void setCfIntra(boolean cfIntra) {
        this.cfIntra = cfIntra
    }

    boolean getCfInter() {
        return cfInter
    }

    void setCfInter(boolean cfInter) {
        this.cfInter = cfInter
    }

    boolean getOaIntra() {
        return oaIntra
    }

    void setOaIntra(boolean oaIntra) {
        this.oaIntra = oaIntra
    }

    boolean getOaIntraPA() {
        return oaIntraPA
    }

    void setOaIntraPA(boolean oaIntraPA) {
        this.oaIntraPA = oaIntraPA
    }

    boolean getOaInter() {
        return oaInter
    }

    void setOaInter(boolean oaInter) {
        this.oaInter = oaInter
    }

    boolean getOaInterPA() {
        return oaInterPA
    }

    void setOaInterPA(boolean oaInterPA) {
        this.oaInterPA = oaInterPA
    }

    boolean getDfpIntra() {
        return dfpIntra
    }

    void setDfpIntra(boolean dfpIntra) {
        this.dfpIntra = dfpIntra
    }

    boolean getDfpInter() {
        return dfpInter
    }

    void setDfpInter(boolean dfpInter) {
        this.dfpInter = dfpInter
    }

    boolean getCd() {
        return cd
    }

    void setCd(boolean cd) {
        this.cd = cd
    }

    boolean getCde() {
        return cde
    }

    void setCde(boolean cde) {
        this.cde = cde
    }

    boolean getPdg() {
        return pdg
    }

    void setPdg(boolean pdg) {
        this.pdg = pdg
    }

    boolean getPdge() {
        return pdge
    }

    void setPdge(boolean pdge) {
        this.pdge = pdge
    }


    boolean getCdPA() {
        return cdPA
    }

    void setCdPA(boolean cdPA) {
        this.cdPA = cdPA
    }

    boolean getCdePA() {
        return cdePA
    }

    void setCdePA(boolean cdePA) {
        this.cdePA = cdePA
    }

    boolean getPdgPA() {
        return pdgPA
    }

    void setPdgPA(boolean pdgPA) {
        this.pdgPA = pdgPA
    }

    boolean getPdgePA() {
        return pdgePA
    }

    void setPdgePA(boolean pdgePA) {
        this.pdgePA = pdgePA
    }


    boolean getPessimisticDataflow() {
        return pessimisticDataflow
    }

    void setPessimisticDataflow(boolean pessimisticDataflow) {
        this.pessimisticDataflow = pessimisticDataflow
    }

    boolean getReachability() {
        return reachability
    }

    void setReachability(boolean reachability) {
        this.reachability = reachability
    }

    boolean getIsHelp() {
        return isHelp
    }
    boolean isReport() {
        return report
    }
    void setReport(boolean report) {
        this.report = report
    }

    int getTimeout() {
        return timeout
    }

    void setTimeout(long timeout) {
        this.timeout = timeout
    }
}