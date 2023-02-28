package com.leron.api.responses;

import java.text.MessageFormat;
import javax.print.attribute.standard.Severity;

public class BaseException extends Exception {
    private static final long serialVersionUID = 2594220962674232227L;
    protected String message;
    protected String input;
    private Severity severity;
    private Object[] parameters;

    protected BaseException(String strCode) {
        super(strCode);
        this.severity = Severity.ERROR;
    }

    protected BaseException(String strCode, String strMessage) {
        super(strCode);
        this.severity = Severity.ERROR;
        this.setMessage(strMessage);
    }

    protected BaseException(String strCode, String strMessage, String input) {
        super(strCode);
        this.severity = Severity.ERROR;
        this.setMessage(strMessage);
        this.setInput(input);
    }

    public BaseException(String strCode, String strMessage, Object... params) {
        super(strCode);
        this.severity = Severity.ERROR;
        this.setParameters(params);
        this.setMessage(strMessage);
    }

    public String getMessege() {
        return this.message;
    }

    public void setMessage(String string) {
        if (this.getParameters() != null && this.getParameters().length > 0) {
            this.message = MessageFormat.format(string, this.getParameters());
        } else {
            this.message = string;
        }

    }

    public Severity getSeverity() {
        return this.severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
