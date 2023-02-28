package com.leron.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(Include.NON_NULL)
public class Response implements Serializable {
    private static final long serialVersionUID = 8927944169026414109L;
    private String severity;
    private String message;
    private String input;
    private String version;

    public Response() {
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setResponse(ApplicationBusinessException e) {
        if (e.getSeverity() != null) {
            this.severity = e.getSeverity().toString();
        }

        if (StringUtils.isNotBlank(e.getMessege())) {
            this.message = e.getMessege();
        }

        if (StringUtils.isNotBlank(e.getInput())) {
            this.input = e.getInput();
        }

    }

    public long length() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(this);
            out.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return (long)(baos.toByteArray().length / 1024);
    }
}
