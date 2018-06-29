package com.example.infinitylabs.dynamictrackerapp.request_response;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"mobile_no",
"access-token"
})
public class TaskRequest {

@JsonProperty("mobile_no")
private String mobileNo;
@JsonProperty("access-token")
private String accessToken;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("mobile_no")
public String getMobileNo() {
return mobileNo;
}

@JsonProperty("mobile_no")
public void setMobileNo(String mobileNo) {
this.mobileNo = mobileNo;
}

@JsonProperty("access-token")
public String getAccessToken() {
return accessToken;
}

@JsonProperty("access-token")
public void setAccessToken(String accessToken) {
this.accessToken = accessToken;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}