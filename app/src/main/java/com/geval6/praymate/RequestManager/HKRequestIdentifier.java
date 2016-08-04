package com.geval6.praymate.RequestManager;

import java.util.HashMap;

public class HKRequestIdentifier {
    public static final String ROOT_URL = "http://52.32.11.69/";
    public static final String WEB_SERVICE = "praymate/";
    public static final String kParameterChangePassword = "change_password";
    public static final String kParameterCount = "count";
    public static final String kParameterDistance = "distance";
    public static final String kParameterEmail = "email";
    public static final String kParameterError = "error";
    public static final String kParameterFeatured = "featured_temple";
    public static final String kParameterLastname = "lastname";
    public static final String kParameterLatitude = "latitude";
    public static final String kParameterLongitude = "longitude";
    public static final String kParameterMessage = "message";
    public static final String kParameterMobile = "mobile";
    public static final String kParameterName = "name";
    public static final String kParameterPackageId = "pid";
    public static final String kParameterPackageName = "package_name";
    public static final String kParameterPassword = "password";
    public static final String kParameterReset = "reset_by";
    public static final String kParameterResponse = "response";
    public static final String kParameterStatus = "status";
    public static final String kParameterTag = "tag";
    public static final String kParameterTempleArea = "address";
    public static final String kParameterTempleID = "tid";
    public static final String kParameterTempleId = "id";
    public static final String kParameterTempleLatitude = "temple_latitude";
    public static final String kParameterTempleLongitude = "temple_longitude";
    public static final String kParameterTempleName = "name";
    public static final String kParameterToken = "token";
    public static final String kParameterUserId = "id";

    public enum HKIdentifier {
        HKIdentifierSignIn,
        HKIdentifierSignUp,
        HKIdentifierForgotPassword,
        HKIdentifierTemplesNearby,
        HKIdentifierForgotResetPassword,
        HKIdentifierTempleSearch,
        HKIdentifierTempleDetail,
        HKIdentifierTemplePackage,
        HKIdentifierTempleFeature,
        HKIdentifierPackageCost,
        HKIdentifierTime
    }

    public static String httpMethodForIdentifier(HKIdentifier identifier) {
        switch (identifier) {
            case HKIdentifierSignIn:
                return "GET";
            case HKIdentifierSignUp:
                return "GET";
            case HKIdentifierForgotPassword:
                return "POST";
            case HKIdentifierTemplesNearby:
                return "GET";
            case HKIdentifierForgotResetPassword:
                return "GET";
            case HKIdentifierTempleSearch:
                return "GET";
            case HKIdentifierTempleDetail:
                return "GET";
            case HKIdentifierTemplePackage:
                return "GET";
            case HKIdentifierTempleFeature:
                return "GET";
            case HKIdentifierPackageCost:
                return "GET";
            case HKIdentifierTime:
                return "GET";
            default:
                return "POST";
        }
    }

    public static String pageForIdentifier(HKIdentifier identifier, HashMap parameters) {
        switch (identifier) {
            case HKIdentifierSignIn:
                return "user_signin.php";
            case HKIdentifierSignUp:
                return "user_signup.php";
            case HKIdentifierForgotPassword:
                return "password_forgot.php";
            case HKIdentifierTemplesNearby:
                return "temple_nearby.php";
            case HKIdentifierForgotResetPassword:
                return "password_reset.php";
            case HKIdentifierTempleSearch:
                return "temple_bytag.php";
            case HKIdentifierTempleDetail:
                return "temple_detail.php";
            case HKIdentifierTemplePackage:
                return "temple_package.php";
            case HKIdentifierTempleFeature:
                return "temple_feature.php";
            case HKIdentifierPackageCost:
                return "package_cost.php";
            case HKIdentifierTime:
                return "package_time.php";
            default:
                return "";
        }
    }

    public static String[] parametersForIdentifier(HKIdentifier identifier) {
        switch (identifier) {
            case HKIdentifierSignIn:
                return new String[]{kParameterEmail, kParameterPassword};
            case HKIdentifierSignUp:
                return new String[]{kParameterTempleName, kParameterMobile, kParameterEmail, kParameterPassword};
            case HKIdentifierForgotPassword:
                return new String[]{kParameterEmail};
            case HKIdentifierTemplesNearby:
                return new String[]{kParameterLatitude, kParameterLongitude, kParameterDistance};
            case HKIdentifierForgotResetPassword:
                return new String[]{kParameterToken, kParameterChangePassword};
            case HKIdentifierTempleSearch:
                return new String[]{kParameterTag};
            case HKIdentifierTempleDetail:
                return new String[]{kParameterUserId};
            case HKIdentifierTemplePackage:
                return new String[]{kParameterTempleID};
            case HKIdentifierTempleFeature:
                return new String[]{kParameterPackageName, kParameterTempleID};
            case HKIdentifierPackageCost:
                return new String[]{kParameterTempleID, kParameterPackageId, kParameterCount};
            case HKIdentifierTime:
                return new String[]{kParameterTempleID, kParameterPackageId};
            default:
                return new String[0];
        }
    }
}
