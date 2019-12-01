package ru.yourport.scheduler1c;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    String res;
    TextView tvName;
    EditText etLogin, etPassword;
    //ArrayAdapter<String> adapter;
    SimpleAdapter adapter;
    ListView lvMain;

    final String ATTRIBUTE_NAME_TIME = "time";
    final String ATTRIBUTE_NAME_CHASSIS = "chassis";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_ID = "id";

    // картинки для отображения динамики
    final int positive = android.R.drawable.arrow_up_float;
    final int negative = android.R.drawable.arrow_down_float;

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TIME, ATTRIBUTE_NAME_CHASSIS, ATTRIBUTE_NAME_IMAGE,
            ATTRIBUTE_NAME_ID };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.tvTime, R.id.tvChassis, R.id.ivImg, R.id.tvID };

    int myYear = 2019;
    int myMonth = 01;
    int myDay = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvName = findViewById(R.id.tvName);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        lvMain = findViewById(R.id.lvMain);

        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        // упаковываем данные в понятную для адаптера структуру
        //ArrayList<Map<String, Object>> data = new ArrayList<>(0);
        // создаем адаптер
        //adapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        //String[] names = {"1"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        //        android.R.layout.simple_list_item_1, names);

        //etLogin.setText("wsChangeServis");

        Button btnSoap = findViewById(R.id.btnSoap);
        btnSoap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLoader dl = new DataLoader();
                dl.execute(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvTime = view.findViewById(R.id.tvTime);
                TextView tvID = view.findViewById(R.id.tvID);
                String info = "itemClick: position = " + position + ", id = " + id +
                        ", text = " + tvTime.getText().toString() +
                        ", id = " + tvID.getText().toString();
                Log.d(LOG_TAG, info);
                Toast.makeText(view.getContext(), info, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clickTest(View view) {

        Toast toast = Toast.makeText(this, "Тест", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        toastContainer.addView(imageView, 0);
        //toast.show();

        Log.d(LOG_TAG, "clickTest");
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //lvMain.setAdapter(adapter);
        //adapter.remove();

        showDialog(1);
    }


    protected Dialog onCreateDialog(int id) {
        if (id == 1) {

            Date dateNow = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String sDate = formatDate.format(dateNow);
            String[] mDate = sDate.split("-");
            myYear = Integer.parseInt(mDate[0]);
            myMonth = Integer.parseInt(mDate[1])-1; //месяц нумеруется с нуля
            myDay = Integer.parseInt(mDate[2]);

            //myMonth = Integer.parseInt(mDate[1]);
            //Toast.makeText(this, mDate.toString(), Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "mDate = " + mDate.toString() + ", sDate = " + sDate);
            for (String d : mDate) {
                Log.d(LOG_TAG, "d = " + d);
            }

            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;
            tvName.setText("Today is " + myDay + "." + myMonth + "." + myYear);
        }
    };

    public class DataLoader extends AsyncTask<String, Integer, String[][]> {

        private static final String NAMESPACE = "http://web/tfk/ExchangeTFK";
        private static final String URL = "http://kamaz.ddns.net:10100/testut/ws/ExchangeTFK.1cws";
        private static final String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:SayHello";
        private static final String METHOD_NAME = "SayHello";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String[][] s) {
            super.onPostExecute(s);
            //tvName.setText(s);
            //Object obj = new JSON
            //tvName.setText(name);
            //String[] names = {"1"};

            //ArrayList ss = new ArrayList();
            //ss.add("Тест1");
            //ss.add("Тест2");

            //adapter = new ArrayAdapter<>(adapter.getContext(),
            //        android.R.layout.simple_list_item_1, s); //или через ArrayList ss

            ArrayList<Map<String, Object>> data = new ArrayList<>(s.length);
            Map<String, Object> m;
            for (int i = 0; i < s.length; i++) {
                m = new HashMap();
               m.put(ATTRIBUTE_NAME_ID, s[i][0]);
                m.put(ATTRIBUTE_NAME_TIME, s[i][1]);
                m.put(ATTRIBUTE_NAME_CHASSIS, "");
                m.put(ATTRIBUTE_NAME_IMAGE, positive);
                data.add(m);
            }

            adapter = new MySimpleAdapter(getApplicationContext(), data, R.layout.item, from, to);

            //ImageView imageView = new ImageView(getApplicationContext());
            //imageView.setImageResource(positive);
            //imageView.setBackgroundColor(Color.RED);
            //adapter.setViewImage(imageView, positive);

            lvMain.setAdapter(adapter);

        }

        @Override
        protected String[][] doInBackground(String... arg) {
            String[][] resultString = new String[0][0];
            String ERROR = "";
            String LOGIN = "wsChangeServis";//arg[0]
            String PASSWORD = "Service2018";//arg[1]
            //String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:Operation";
            String METHOD_NAME = "Операция";//Выполнить Operation SayHello
            //Log.d(LOG_TAG, "Login: " + LOGIN);
            //Log.d(LOG_TAG, "Password: " + PASSWORD);

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("Запрос", "СписокОрганизаций");
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

                //SoapObject query = new SoapObject(NAMESPACE, "ПолучитьСписокОбъектовЗапроса");
                //query.addProperty("Тип", "ЭлектроннаяОчередь");
                //request.addSoapObject(query);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER12);
                // Тоже важный элемент - не выводит типы данных в элементах xml
                envelope.implicitTypes = true;
                envelope.setOutputSoapObject(request);

                HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(
                        URL, LOGIN, PASSWORD);
                androidHttpTransport.debug = true;

                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    //SoapObject resultsRequestSoap = (SoapObject) envelope.bodyIn;
                    //System.out.println("Response::" + resultsRequestSoap.toString());
                    //resultString = "Response::" + resultsRequestSoap.toString();
                    String result = envelope.getResponse().toString();
                    Log.d(LOG_TAG, "Result: " + result);

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray ja = jsonObject.getJSONArray("МассивОрганизаций");

                    resultString = new String[ja.length()+1][2];
                    //for (String j : ja) {
                    //
                    //}

                    //Map<String, String> m;

                    resultString[0][1] = jsonObject.getString("Текст");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject joOrg = ja.getJSONObject(i);
                        String id = joOrg.getString("ID");
                        String name = joOrg.getString("Наименование");
                        Log.d(LOG_TAG, "ID = " + id + ", Наименование = " + name);

                        resultString[i+1][0] = id;
                        resultString[i+1][1] = name;
                    }

                } catch (Exception e) {
                    Log.d(LOG_TAG, e.getClass() + " HTTP TRANSPORT error: " + e.getMessage());
                    ERROR = e.getMessage();
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
                ERROR = e.getMessage();
                e.printStackTrace();
            }

            int index401 = ERROR.indexOf("HTTP status: 401");
            int indexHost = ERROR.indexOf("Unable to resolve host");
            if (index401 > -1) {
                ERROR = "Не пройдена авторизация";
                Log.d(LOG_TAG, ERROR);
            } else if (indexHost > -1) {
                ERROR = "Не найден хост";
                Log.d(LOG_TAG, ERROR);
            }

            //System.out.println(resultString);

            return resultString;
        }

        class MySimpleAdapter extends SimpleAdapter {

            public MySimpleAdapter(Context context,
                                   List<? extends Map<String, ?>> data, int resource,
                                   String[] from, int[] to) {
                super(context, data, resource, from, to);
            }

            @Override
            public void setViewText(TextView v, String text) {
                // метод супер-класса, который вставляет текст
                super.setViewText(v, text);
                // если нужный нам TextView, то разрисовываем
                //if (v.getId() == R.id.tvValue) {
                //    int i = Integer.parseInt(text);
                //    if (i < 0) v.setTextColor(Color.RED); else
                //    if (i > 0) v.setTextColor(Color.GREEN);
                //}
            }

            @Override
            public void setViewImage(ImageView v, int value) {
                // метод супер-класса
                super.setViewImage(v, value);
                // разрисовываем ImageView
                if (value == negative) v.setBackgroundColor(Color.RED); else
                if (value == positive) v.setBackgroundColor(Color.GREEN);
            }
        }
    }

//    public class JsonParser {
//
//        public Resultat getResultat(String response) throws JSONException {
//            JSONObject jsonObject = new JSONObject(response);
//            String name = jsonObject.getString("Текст");
//
//            return new Resultat(name);
//        }
//    }
//
//    public class Resultat() {
//        public String string {
//            return string;
//        }
//    }

}