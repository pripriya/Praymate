package com.geval6.praymate.Utlis;

public class Temple<T> {
    String temple_area;
    String temple_id;
    String temple_lat;
    String temple_long;
    String temple_name;

    public Temple(String temple_id, String temple_name, String temple_area, String temple_lat, String temple_long) {
        this.temple_id = temple_id;
        this.temple_name = temple_name;
        this.temple_area = temple_area;
        this.temple_lat = temple_lat;
        this.temple_long = temple_long;
    }

    public String getTemple_id() {
        return this.temple_id;
    }

    public String getTemple_name() {
        return this.temple_name;
    }

    public String getTemple_area() {
        return this.temple_area;
    }

    public String getTemple_lat() {
        return this.temple_lat;
    }

    public String getTemple_long() {
        return this.temple_long;
    }
}
