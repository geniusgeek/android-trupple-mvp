package com.github.geniusgeek.trupple_mvp.exceptions;

import com.github.geniusgeek.trupple_mvp.common.Builder;
import com.github.geniusgeek.trupple_mvp.common.BuilderBuild;

/**
 * Created by root on 2/19/17.
 * This class serves as the response recieved from an http request
 */

public final class ErrorResponse {
    private String body;
    private String reason;
    private int responseCode;

    public ErrorResponse(int responseCode, String  reason,String body) {
        this.body = body;
        this.reason = reason;
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResponseCode() {
        return responseCode;
    }

    private void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Throwable getException(){
        return new Throwable(getReason());
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "body='" + getBody() + '\'' +
                ", message='" + getReason() + '\'' +
                ", responseCode=" + getResponseCode() +
                '}';
    }

    public  static class ErrorResponseBuilder implements Builder<ErrorResponse>,BuilderBuild<ErrorResponseBuilder,ErrorResponse> {
        private String body;
        private String reason;
        private int responseCode;

        public ErrorResponseBuilder body(String body) {
            this.body=body;
            return this;
        }
        public ErrorResponseBuilder message(String reason) {
            this.reason = reason;
            return this;
        }
        public ErrorResponseBuilder responseCode(int responseCode) {
            this.responseCode = responseCode;
            return this;
        }


        @Override
        public ErrorResponse build(ErrorResponseBuilder builder) {
            return null;
        }


        @Override
        public ErrorResponse build() {
            return new ErrorResponse(responseCode,reason,body);
        }


    }
}
