package ru.yourport.scheduler1c;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    String res;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvName = findViewById(R.id.tvName);

        Button btnSoap = findViewById(R.id.btnSoap);
        btnSoap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLoader dl = new DataLoader();
                dl.execute();
            }
        });
    }

    public class DataLoader extends AsyncTask<Void, Void, String> {

        private static final String NAMESPACE = "http://web/tfk/ExchangeTFK";
        private static final String URL = "http://kamaz.ddns.net:10100/testut/ws/ExchangeTFK.1cws";
        private static final String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:SayHello";
        private static final String METHOD_NAME = "SayHello";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvName.setText(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String resultString = "";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                //request.addProperty("id", sale.getId());
                //SimpleDateFormat dateFormat = new SimpleDateFormat(
                //        "yyyy-MM-dd'T'HH:mm:ss");
                //request.addProperty("date", dateFormat.format(sale.getDate()));
                //request.addProperty("clientCardNumber", sale.getCardNumber());
                //request.addProperty("bonuses", Double.toString(sale.getBonuses()));
                //...
                //
                // see - http://code.google.com/p/ksoap2-android/wiki/CodingTipsAndTricks#Adding_an_array_of_complex_objects_to_the_request
                //SoapObject sales = new SoapObject(NAMESPACE, "items");
                //for (SaleItemInformation item : sale.getSales()) {
                //    SoapObject itemSoap = new SoapObject(NAMESPACE, "Items");
                //    itemSoap.addProperty("Code", item.getItem().getSourceCode());
                //    itemSoap.addProperty("Quantity", Double.toString(item.getQuantity()));
                //    //...
                //    sales.addSoapObject(itemSoap);
                //}
                //request.addSoapObject(sales);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER12);
                // Тоже важный элемент - не выводит типы данных в элементах xml
                envelope.implicitTypes = true;
                envelope.setOutputSoapObject(request);

                HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(
                        URL, "wsChangeServis", "Service2018");
                androidHttpTransport.debug = true;

                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    //SoapObject resultsRequestSoap = (SoapObject) envelope.bodyIn;
                    //System.out.println("Response::" + resultsRequestSoap.toString());
                    //resultString = resultsRequestSoap.toString();
                    resultString = envelope.getResponse().toString();
                    Log.d(LOG_TAG, resultString);
                } catch (Exception e) {
                    Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
                e.printStackTrace();
            }

            return resultString;
        }
    }
}