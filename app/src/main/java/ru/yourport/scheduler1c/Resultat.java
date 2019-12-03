package ru.yourport.scheduler1c;

import android.os.AsyncTask;

public class Resultat {
    String name;
    int age;
    String[][] result;

    public String getResultat() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String[][] getResult() {

        return result;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Resultat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Resultat() {
    }

    public Resultat(String name) {
        this.name = name;

    }

    public class DataLoader1 extends AsyncTask<String, Integer, String[][]> {

        private static final String NAMESPACE = "http://web/tfk/ExchangeTFK";
        private static final String URL = "http://kamaz.ddns.net:10100/testut/ws/ExchangeTFK.1cws";
        private static final String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:SayHello";
        private static final String METHOD_NAME = "SayHello";

        @Override
        protected void onPostExecute(String[][] strings) {
            super.onPostExecute(strings);

            result = strings;
        }

        @Override
        protected String[][] doInBackground(String... strings) {
            return new String[0][0];
        }
    }
}
