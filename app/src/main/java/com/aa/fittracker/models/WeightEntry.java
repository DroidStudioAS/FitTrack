package com.aa.fittracker.models;



    public class WeightEntry {

        private String weight_date;
        private String weight_value;

        public WeightEntry() {
        }

        public WeightEntry(String weight_date, String weight_value) {
            this.weight_date = weight_date;
            this.weight_value = weight_value;
        }

        public String getWeight_date() {
            return weight_date;
        }

        public void setWeight_date(String weight_date) {
            this.weight_date = weight_date;
        }

        public String getWeight_value() {
            return weight_value;
        }

        public void setWeight_value(String weight_value) {
            this.weight_value = weight_value;
        }
    }


