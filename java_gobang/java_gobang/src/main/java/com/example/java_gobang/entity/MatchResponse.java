package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class MatchResponse {

    private boolean OK;
    private String reason;
    private String message;

    public static MatchResponse success(boolean OK, String reason, String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOK(OK);
        matchResponse.setReason(reason);
        matchResponse.setMessage(message);
        return matchResponse;
    }

    public static MatchResponse success(boolean OK, String reason) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOK(OK);
        matchResponse.setReason(reason);
        matchResponse.setMessage("");
        return matchResponse;
    }

    public static MatchResponse success(String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOK(true);
        matchResponse.setReason("");
        matchResponse.setMessage(message);
        return matchResponse;
    }

    public static MatchResponse fail(boolean OK, String reason) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOK(OK);
        matchResponse.setReason(reason);
        matchResponse.setMessage("");
        return matchResponse;
    }

    public static MatchResponse fail(boolean OK, String reason, String message) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setOK(OK);
        matchResponse.setReason(reason);
        matchResponse.setMessage(message);
        return matchResponse;
    }
}
