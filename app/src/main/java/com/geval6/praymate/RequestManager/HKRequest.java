package com.geval6.praymate.RequestManager;

import com.geval6.praymate.RequestManager.HKRequestIdentifier.HKIdentifier;
import java.util.HashMap;

public class HKRequest {
    public String contentType;
    public HKIdentifier identifier;
    public HashMap parameters;
    private Object responseObject;
    public String responseString;

    public HKRequest(HKIdentifier identifier, HashMap parameters) {
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public Object getResponseObject() {
        if (this.responseObject == null && this.contentType != null && this.contentType.equalsIgnoreCase("application/json")) {
            this.responseObject = HKFunctions.objectFromJson(this.responseString);
        }
        return this.responseObject;
    }
}
