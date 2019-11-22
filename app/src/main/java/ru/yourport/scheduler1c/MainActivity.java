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
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
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
        protected String doInBackground(Void... voids) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER12);
                //request.addProperty("IsFirstReguest", true);
                envelope.setOutputSoapObject(request);

                HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(
                        URL, "wsChangeServis", "Service2018");
                androidHttpTransport.debug = true;

                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapObject resultsRequestSoap = (SoapObject) envelope.bodyIn;
                    //System.out.println("Response::" + resultsRequestSoap.toString());
                    //tvName.setText(resultsRequestSoap.toString());
                    Log.d(LOG_TAG, resultsRequestSoap.toString());
                } catch (Exception e) {
                    Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
                e.printStackTrace();
            }

            return "";
        }
    }

    public class HttpTransportBasicAuthSE extends HttpTransportSE {
        private String username;
        private String password;

        /**
         * Constructor with username and password
         *
         * @param url
         *            The url address of the webservice endpoint
         * @param username
         *            Username for the Basic Authentication challenge RFC 2617
         * @param password
         *            Password for the Basic Authentication challenge RFC 2617
         */
        public HttpTransportBasicAuthSE(String url, String username, String password) {
            super(url);
            this.username = username;
            this.password = password;
        }

        public ServiceConnection getServiceConnection() throws IOException {
            ServiceConnectionSE midpConnection = new ServiceConnectionSE(url);
            addBasicAuthentication(midpConnection);
            return midpConnection;
        }

        protected void addBasicAuthentication(ServiceConnection midpConnection) throws IOException {
            if (username != null && password != null) {
                StringBuffer buf = new StringBuffer(username);
                buf.append(':').append(password);
                byte[] raw = buf.toString().getBytes();
                buf.setLength(0);
                buf.append("Basic ");
                org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
                midpConnection.setRequestProperty("Authorization", buf.toString());
            }
        }
    }
}
