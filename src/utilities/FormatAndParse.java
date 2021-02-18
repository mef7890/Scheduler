package utilities;

import java.math.BigDecimal;
import java.time.LocalTime;

public class FormatAndParse {

    public static int hour(LocalTime time) {
        int hr = Integer.parseInt(time.toString().substring(0, 2));
        return hr;
    }

    public static String minute(LocalTime time) {
        String min = time.toString().substring(3, 5);
        return min;
    }

    // todo, might not need since not using month Enum
    public static String formatTimeFrame(String string) { // takes the all caps enum and returns time frame with correct caps
        // to use in sentence
        if (string.equalsIgnoreCase("This Week") ||
            string.equalsIgnoreCase("All Time")) { // make "this week"/"all time" lowercase
            string = string.toLowerCase();
        } else {
            string = string.toLowerCase(); // capitalize month
            String firstLetter = string.substring(0, 1);
            String firstLetterCaps = firstLetter.toUpperCase();
            string = string.replaceFirst(firstLetter, firstLetterCaps);
            string = "in " + string;
        }
        return string;
    }

    // remove '$' and ensure .xx
    public static String parseRateString(String rate) { // todo give warning if too many or too few characters?

        if (String.valueOf(rate.charAt(0)).equalsIgnoreCase("$")) { // remove $ sign
            rate = rate.substring(1);
        }
        if (!rate.contains(".")) { // if just dollars, no cents, add .00
            rate = rate + ".00";
        }

        int index = (rate.indexOf(".") + 1); // doesn't include . in substring

        if (rate.substring(index).length() > 2) { // if >2 precision points, truncate (not round)
            rate = rate.substring(0, (index + 2));
        }
        if (rate.substring(index).length() == 1) { // if missing precision point, just add zero
            rate = rate + "0";
        }
        return rate;

    }

    public static BigDecimal parseRate(String rateString) {
        BigDecimal rate = new BigDecimal(rateString).setScale(2);
        return rate;
    }

    public static String parsePhone(String phoneNum) {
        // ----------   FORMAT PHONE # INPUT ---------- //
            try {
                for (int i = 0; i < phoneNum.length(); i++) {
                    if (!Character.isDigit(phoneNum.charAt(i))) { // check if it's not a digit
                        char x = phoneNum.charAt(i);
                        phoneNum = phoneNum.replace(x, ' ');
                        System.out.println("phone num in parse phone before replacing spaces = " + phoneNum); // todo del
                    }
                }
                phoneNum = phoneNum.replaceAll("\\s", ""); // delete spaces
                System.out.println("phone num in parse phone after replacing spaces = " + phoneNum); // todo del

                phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 6) + "-" + phoneNum.substring(6, phoneNum.length());
                System.out.println("phone num in parse phone after formatting  = " + phoneNum); // todo del

                return phoneNum;

            } catch (StringIndexOutOfBoundsException e) {
                Alerts.phoneLengthError();
            }
//        }
        return "";
    }


}
