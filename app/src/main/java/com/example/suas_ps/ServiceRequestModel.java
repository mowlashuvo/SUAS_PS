package com.example.suas_ps;

import android.widget.EditText;

import java.util.Date;
import java.util.Objects;

public class ServiceRequestModel {
    private final String room_no;
    private final String contact_no;
    private final String description;
    private final String service_image;
    private final String status;
    private final String current_time;

    public ServiceRequestModel(String room_no, String contact_no, String description, String service_image, String status, String current_time) {
        this.room_no = room_no;
        this.contact_no = contact_no;
        this.description = description;
        this.service_image = service_image;
        this.status = status;
        this.current_time = current_time;
    }

    public String getRoom_no() {
        return room_no;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getDescription() {
        return description;
    }

    public String getService_image() {
        return service_image;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrent_time() {
        return current_time;
    }
}
