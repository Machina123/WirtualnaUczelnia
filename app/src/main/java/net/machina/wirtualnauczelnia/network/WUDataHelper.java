package net.machina.wirtualnauczelnia.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import net.machina.wirtualnauczelnia.R;
import net.machina.wirtualnauczelnia.common.ApiDetails;
import net.machina.wirtualnauczelnia.common.Constants;
import net.machina.wirtualnauczelnia.common.DatasetFields;
import net.machina.wirtualnauczelnia.common.LoginDetails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WUDataHelper {
    private static WUDataHelper instance;
    private Context mContext;
    private OkHttpClient httpClient;
    private boolean previousTermGradesAvailable = false, nextTermGradesAvailable = false;
    private boolean previousWeekScheduleAvailable = false, nextWeekScheduleAvailable = false;

    private String mViewState, mViewStateGenerator, mEventValidation;

    public WUDataHelper(Context mContext) {
        this.mContext = mContext;
        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this.mContext));
        httpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        init();
    }

    public static synchronized WUDataHelper getInstance(Context mContext) {
        if(instance == null) {
            instance = new WUDataHelper(mContext);
        }

        return instance;
    }

    public boolean isPreviousTermGradesAvailable() {
        return previousTermGradesAvailable;
    }

    public boolean isNextTermGradesAvailable() {
        return nextTermGradesAvailable;
    }

    private void setPreviousTermGradesAvailable(boolean previousTermGradesAvailable) {
        this.previousTermGradesAvailable = previousTermGradesAvailable;
    }

    private void setNextTermGradesAvailable(boolean nextTermGradesAvailable) {
        this.nextTermGradesAvailable = nextTermGradesAvailable;
    }

    public boolean isPreviousWeekScheduleAvailable() {
        return previousWeekScheduleAvailable;
    }

    public void setPreviousWeekScheduleAvailable(boolean previousWeekScheduleAvailable) {
        this.previousWeekScheduleAvailable = previousWeekScheduleAvailable;
    }

    public boolean isNextWeekScheduleAvailable() {
        return nextWeekScheduleAvailable;
    }

    public void setNextWeekScheduleAvailable(boolean nextWeekScheduleAvailable) {
        this.nextWeekScheduleAvailable = nextWeekScheduleAvailable;
    }

    private void init() {
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .url(ApiDetails.API_ADDRESS_LOGIN)
                .head()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(Constants.LOGGER_TAG, "WUDataHelper/init - received cookies");
            }
        });
    }

    public void login(LoginDetails loginDetails, final OnNetworkDataReceivedListener listener) {

        FormBody formData = new FormBody.Builder()
                .addEncoded(ApiDetails.API_FIELD_LOGIN, Uri.encode(loginDetails.getIdentifier()))
                .addEncoded(ApiDetails.API_FIELD_PASSWORD, Uri.encode(loginDetails.getPassword()))
                .addEncoded(ApiDetails.API_FIELD_LOGIN_BTN, Uri.encode(ApiDetails.API_DEF_VALUE_LOGIN_BTN))
                .add(ApiDetails.API_FIELD_VIEWSTATE, "")
                .build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(ApiDetails.API_ADDRESS_LOGIN)
                .method("POST", formData)
                .build();

        try {
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    listener.onDataReceived(false, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String sessionId = response.headers().get("Set-Cookie");
                    Log.d(Constants.LOGGER_TAG, "cookies: " + sessionId);
                    if(response.request().url().toString().equals(ApiDetails.API_ADDRESS_LOGIN)) {
                        Document loadedPage = Jsoup.parse(response.body().string());
                        Element errorElement = loadedPage.body().selectFirst(ApiDetails.API_FIELD_LOGIN_ERROR);
                        listener.onDataReceived(false, errorElement != null ? errorElement.ownText() : mContext.getString(R.string.error_login));
                    } else {
                        Matcher matcher = Pattern.compile("[0-9A-F]{8,}").matcher(sessionId);
                        matcher.find();
                        listener.onDataReceived(true, matcher.group(0));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onDataReceived(false, ex.getMessage());
        }
    }

    public void logout(final OnNetworkDataReceivedListener listener) {
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .get()
                .url(ApiDetails.API_ADDRESS_LOGOUT)
                .build();
        try {
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onDataReceived(false, null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.request().url().toString().equals(ApiDetails.API_ADDRESS_LOGIN)) listener.onDataReceived(true, null);
                    else listener.onDataReceived(false, null);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onDataReceived(false, ex.getMessage());
        }
    }

    public void getLoggedInUser(final OnNetworkDataReceivedListener listener) {
        try {
            Request request = new Request.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .url(ApiDetails.API_ADDRESS_STUDENT_DATA)
                    .get()
                    .build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onDataReceived(false,"null");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    Document document = Jsoup.parse(response.body().string());
                    Element element = document.selectFirst(ApiDetails.API_SELECTOR_WHOISLOGGEDIN);
                    if(element != null) listener.onDataReceived(true, element.ownText());
                    else {
                        Log.e(Constants.LOGGER_TAG, "WUDataHelper/getLoggedInUser - element is null");
                        Log.d(Constants.LOGGER_TAG, "WUDataHelper/getLoggedInUser - redirected to " + response.request().url().toString());
                        listener.onDataReceived(false,"null");
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            listener.onDataReceived(false,"null");
        }
    }

    public void getCurrentTermGrades(final OnNetworkDataSetReceivedListener listener) {
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .url(ApiDetails.API_ADDRESS_GRADES)
                .get()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseGradeResponse(response, listener);
            }
        });
    }

    public void getPreviousTermGrades(final OnNetworkDataSetReceivedListener listener) {
        if(!isPreviousTermGradesAvailable()) {
            listener.onDatasetReceived(false, null);
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add(ApiDetails.API_FIELD_BUTTON_GRADES_PREV, ApiDetails.API_DEF_VALUE_BUTTON_PREV)
                .add(ApiDetails.API_FIELD_VIEWSTATE, mViewState)
                .add(ApiDetails.API_FIELD_VIEWSTATE_GENERATOR, mViewStateGenerator)
                .add(ApiDetails.API_FIELD_EVENT_VALIDATION, mEventValidation)
                .build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(ApiDetails.API_ADDRESS_GRADES)
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseGradeResponse(response, listener);
            }
        });
    }

    public void getNextTermGrades(final OnNetworkDataSetReceivedListener listener) {
        if(!isNextTermGradesAvailable()) {
            listener.onDatasetReceived(false, null);
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add(ApiDetails.API_FIELD_BUTTON_GRADES_NEXT, ApiDetails.API_DEF_VALUE_BUTTON_NEXT)
                .add(ApiDetails.API_FIELD_VIEWSTATE, mViewState)
                .add(ApiDetails.API_FIELD_VIEWSTATE_GENERATOR, mViewStateGenerator)
                .add(ApiDetails.API_FIELD_EVENT_VALIDATION, mEventValidation)
                .build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(ApiDetails.API_ADDRESS_GRADES)
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseGradeResponse(response, listener);
            }
        });
    }

    private void parseGradeResponse(Response response, OnNetworkDataSetReceivedListener listener) {
        try {
            Log.d(Constants.LOGGER_TAG, "WUDataHelper/parseGradeResponse - response url: " + response.request().url().toString());
            Document document = Jsoup.parse(response.body().string());
            Elements elements = document.select(ApiDetails.API_SELECTOR_GRID_DATAROW);
            if (elements != null) {
                mViewState = document.selectFirst(ApiDetails.API_SELECTOR_VIEWSTATE).val();
                mViewStateGenerator = document.selectFirst(ApiDetails.API_SELECTOR_VIEWSTATEGEN).val();
                mEventValidation = document.selectFirst(ApiDetails.API_SELECTOR_EVENTVALIDATION).val();

                Element btnPrevious = document.selectFirst(ApiDetails.API_SELECTOR_GRADES_BUTTON_PREV);
                if(btnPrevious != null) this.setPreviousTermGradesAvailable(true);
                else this.setPreviousTermGradesAvailable(false);

                Element btnNext = document.selectFirst(ApiDetails.API_SELECTOR_GRADES_BUTTON_NEXT);
                if(btnNext != null) this.setNextTermGradesAvailable(true);
                else this.setNextTermGradesAvailable(false);

                ArrayList<HashMap<String, String>> list = new ArrayList<>();

                HashMap<String, String> semesterInfo = new HashMap<>();
                semesterInfo.put(DatasetFields.DS_TERMINFO_TERM, document.selectFirst(ApiDetails.API_SELECTOR_GRADES_CURR_TERM).ownText());
                semesterInfo.put(DatasetFields.DS_TERMINFO_YEAR, document.selectFirst(ApiDetails.API_SELECTOR_GRADES_CURR_YEAR).ownText());
                list.add(semesterInfo);

                for (Element grade : elements) {
                    int fieldIterator = 0;
                    HashMap<String, String> parsedGradeDetails = new HashMap<>();
                    for (Element gradeDetail : grade.children()) {
                        parsedGradeDetails.put(DatasetFields.DS_GRADE_DETAILS[fieldIterator++], gradeDetail.ownText());
                    }
                    list.add(parsedGradeDetails);
                }
                listener.onDatasetReceived(true, list);
            } else listener.onDatasetReceived(false, null);
        } catch(Exception ex) {
            ex.printStackTrace();
            listener.onDatasetReceived(false, null);
        }
    }

    public void getCurrentWeekSchedule(final OnNetworkDataSetReceivedListener listener) {
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .url(ApiDetails.API_ADDRESS_SCHEDULE)
                .get()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseScheduleResponse(response, listener);
            }
        });

    }

    public void getPreviousWeekSchedule(final OnNetworkDataSetReceivedListener listener) {
        if(!isPreviousWeekScheduleAvailable()) {
            listener.onDatasetReceived(false, null);
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add(ApiDetails.API_FIELD_VIEWSTATE, mViewState)
                .add(ApiDetails.API_FIELD_VIEWSTATE_GENERATOR, mViewStateGenerator)
                .add(ApiDetails.API_FIELD_EVENT_VALIDATION, mEventValidation)
                .add(ApiDetails.API_FIELD_SCHEDULE_BUTTON_PREV, ApiDetails.API_DEF_VALUE_BUTTON_PREV)
                .add(ApiDetails.API_FIELD_SCHEDULE_RANGE, ApiDetails.API_DEF_VALUE_SCHEDULE_WEEKLY)
                .build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(ApiDetails.API_ADDRESS_SCHEDULE)
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseScheduleResponse(response, listener);
            }
        });
    }

    public void getNextWeekSchedule(final OnNetworkDataSetReceivedListener listener) {
        if(!isNextWeekScheduleAvailable()) {
            listener.onDatasetReceived(false, null);
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add(ApiDetails.API_FIELD_VIEWSTATE, mViewState)
                .add(ApiDetails.API_FIELD_VIEWSTATE_GENERATOR, mViewStateGenerator)
                .add(ApiDetails.API_FIELD_EVENT_VALIDATION, mEventValidation)
                .add(ApiDetails.API_FIELD_SCHEDULE_BUTTON_NEXT, ApiDetails.API_DEF_VALUE_BUTTON_NEXT)
                .add(ApiDetails.API_FIELD_SCHEDULE_RANGE, ApiDetails.API_DEF_VALUE_SCHEDULE_WEEKLY)
                .build();

        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(ApiDetails.API_ADDRESS_SCHEDULE)
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDatasetReceived(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseScheduleResponse(response, listener);
            }
        });
    }

    private void parseScheduleResponse(Response response, final OnNetworkDataSetReceivedListener listener) {

        try {
            Log.d(Constants.LOGGER_TAG, "WUDataHelper/parseScheduleResponse - response url: " + response.request().url().toString());
            Document document = Jsoup.parse(response.body().string());
            Elements elements = document.select(ApiDetails.API_SELECTOR_GRID_DATAROW);

            if(elements != null) {
                mViewState = document.selectFirst(ApiDetails.API_SELECTOR_VIEWSTATE).val();
                mViewStateGenerator = document.selectFirst(ApiDetails.API_SELECTOR_VIEWSTATEGEN).val();
                mEventValidation = document.selectFirst(ApiDetails.API_SELECTOR_EVENTVALIDATION).val();

                Element btnPrevious = document.selectFirst(ApiDetails.API_SELECTOR_SCHEDULE_BUTTON_PREV);
                if(btnPrevious != null) this.setPreviousWeekScheduleAvailable(true);
                else this.setPreviousWeekScheduleAvailable(false);

                Element btnNext = document.selectFirst(ApiDetails.API_SELECTOR_SCHEDULE_BUTTON_NEXT);
                if(btnNext != null) this.setNextWeekScheduleAvailable(true);
                else this.setNextWeekScheduleAvailable(false);

                HashMap<String, String> weekInfo = new HashMap<>();
                String currentWeekData = document.selectFirst(ApiDetails.API_SELECTOR_SCHEDULE_CURR_WEEK).ownText();
                weekInfo.put(DatasetFields.DS_SCHEDULE_CURR_WEEK, currentWeekData);
                Log.d(Constants.LOGGER_TAG, "WUDataHelper/parseScheduleResponse: currentWeek: " + currentWeekData);

                ArrayList<HashMap<String, String>> scheduleData = new ArrayList<>();
                scheduleData.add(weekInfo);

                for(Element scheduleEntry : elements) {
                    int fieldIterator = 0;
                    HashMap<String, String> parsedScheduleEntry = new HashMap<>();
                    for(Element entryDetails: scheduleEntry.children()) {
                        parsedScheduleEntry.put(DatasetFields.DS_SCHEDULE_DETAILS[fieldIterator++], entryDetails.wholeText());
                    }
                    scheduleData.add(parsedScheduleEntry);
                }

                listener.onDatasetReceived(true, scheduleData);

            } else listener.onDatasetReceived(false, null);

        } catch(Exception ex) {
            ex.printStackTrace();
            listener.onDatasetReceived(false, null);
        }
    }
}
