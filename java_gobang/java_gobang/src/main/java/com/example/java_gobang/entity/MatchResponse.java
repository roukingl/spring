package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class MatchResponse {

    private boolean ok;
    private String reason;
    private String message;

    public static MatchResponse success(boolean ok, String reason, String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOk(ok);
        matchResponse.setReason(reason);
        matchResponse.setMessage(message);
        return matchResponse;
    }

    public static MatchResponse success(boolean ok, String reason) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOk(ok);
        matchResponse.setReason(reason);
        matchResponse.setMessage("");
        return matchResponse;
    }

    public static MatchResponse success(String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOk(true);
        matchResponse.setReason("");
        matchResponse.setMessage(message);
        return matchResponse;
    }

    public static MatchResponse fail(boolean ok, String reason) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOk(ok);
        matchResponse.setReason(reason);
        matchResponse.setMessage("");
        return matchResponse;
    }

    public static MatchResponse fail(boolean ok, String reason, String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOk(ok);
        matchResponse.setReason(reason);
        matchResponse.setMessage(message);
        return matchResponse;
    }
}
