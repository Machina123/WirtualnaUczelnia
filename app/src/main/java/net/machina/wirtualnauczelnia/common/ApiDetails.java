package net.machina.wirtualnauczelnia.common;

public class ApiDetails {

//  Adresy endpointów
    public static final String API_ADDRESS_BASE = "https://wu.up.krakow.pl/WU/";
    public static final String API_ADDRESS_LOGIN = API_ADDRESS_BASE + "Logowanie2.aspx";
    public static final String API_ADDRESS_LOGOUT = API_ADDRESS_BASE + "Wyloguj.aspx";
    public static final String API_ADDRESS_GRADES = API_ADDRESS_BASE + "OcenyP.aspx";
    public static final String API_ADDRESS_STUDENT_DATA = API_ADDRESS_BASE + "Wynik2.aspx";

//  Pola formularzy
    public static final String API_FIELD_LOGIN = "ctl00$ctl00$ContentPlaceHolder$MiddleContentPlaceHolder$txtIdent";
    public static final String API_FIELD_PASSWORD = "ctl00$ctl00$ContentPlaceHolder$MiddleContentPlaceHolder$txtHaslo";
    public static final String API_FIELD_LOGIN_BTN = "ctl00$ctl00$ContentPlaceHolder$MiddleContentPlaceHolder$butLoguj";
    public static final String API_FIELD_LOGIN_ERROR = "ctl00_ctl00_ContentPlaceHolder_MiddleContentPlaceHolder_lblMessage";
    public static final String API_FIELD_BUTTON_GRADES_PREV = "ctl00$ctl00$ContentPlaceHolder$RightContentPlaceHolder$butPop";
    public static final String API_FIELD_BUTTON_GRADES_NEXT = "ctl00$ctl00$ContentPlaceHolder$RightContentPlaceHolder$butNas";

    public static final String API_FIELD_VIEWSTATE = "__VIEWSTATE";
    public static final String API_FIELD_VIEWSTATE_GENERATOR = "__VIEWSTATEGENERATOR";
    public static final String API_FIELD_EVENT_VALIDATION = "__EVENTVALIDATION";

//  Różne obiekty na stronie (selektory)
    public static final String API_SELECTOR_WHOISLOGGEDIN = ".who_is_logged_in";
    public static final String API_SELECTOR_GRID_DATAROW = ".gridDane";
    public static final String API_SELECTOR_GRADES_CURR_TERM = "#ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_lblSemestr";
    public static final String API_SELECTOR_GRADES_CURR_YEAR = "#ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_lblRok";
    public static final String API_SELECTOR_GRADES_BUTTON_NEXT = "#ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_butNas";
    public static final String API_SELECTOR_GRADES_BUTTON_PREV = "#ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_butPop";
    public static final String API_SELECTOR_VIEWSTATE = "input[name=__VIEWSTATE]";
    public static final String API_SELECTOR_VIEWSTATEGEN = "input[name=__VIEWSTATEGENERATOR]";
    public static final String API_SELECTOR_EVENTVALIDATION = "input[name=__EVENTVALIDATION]";

//  Domyślne wartości
    public static final String API_DEF_VALUE_LOGIN_BTN = "Zaloguj";
    public static final String API_DEF_VALUE_GRADES_PREV = "Poprzedni";
    public static final String API_DEF_VALUE_GRADES_NEXT = "Następny";
    public static final String API_DEF_VALUE_EMPTY = "";

//  różne wartości
    public static final String API_NAME_COOKIE_SESSIONID = ".ASPXUSERWU";
}

