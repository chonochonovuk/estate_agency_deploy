package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.view.PropertyViewModel;

import java.util.List;
import java.util.Set;

public class AjaxResponseBody {
    private String message;
    private Set<PropertyViewModel> result;

    public AjaxResponseBody() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<PropertyViewModel> getResult() {
        return result;
    }

    public void setResult(Set<PropertyViewModel> result) {
        this.result = result;
    }
}
