package com.marvel.characters.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import androidx.appcompat.app.AppCompatActivity
import com.marvel.characters.MyApplication
import com.marvel.characters.R
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Time {

    fun getDateFromTimeStamp(timeStamp: String): Date {
        val time = java.lang.Long.parseLong(timeStamp) * 1000
        val stamp = Timestamp(time)
        return Date(stamp.time)
    }

    /**
     * return -1 if first is before , 0 if equals , 1 if second is before
     *
     * @return
     */
    fun compareDates(date1: Date, date2: Date): Int {
        try {

            val sdf = SimpleDateFormat("yyyy-MM-dd")

            val day1 = sdf.format(date1)
            val day2 = sdf.format(date2)

            val date3 = sdf.parse(day1)
            val date4 = sdf.parse(day2)
            if (date3.after(date4)) {
                return 1
            } else if (date3.before(date4)) {
                return -1
            } else if (date3 == date4) {
                return 0
            }
        } catch (ex: ParseException) {
            return 0
        }

        return 0
    }

    fun getMediaTimeInStringBySeconds(durationInSeconds: String): String {
        val result = ""
        try {
            val dur = java.lang.Long.parseLong(durationInSeconds)
            val second = dur % 60
            val minute = dur / 60 % 60
            val hour = dur / (60 * 60) % 24
            val f = Formatter(StringBuilder(8), Locale("en"))
            return if (hour > 0) {
                f.format("%02d:%02d:%02d", hour, minute, second).toString()
            } else {
                f.format("%02d:%02d", minute, second).toString()
            }
        } catch (e: Exception) {
            return result
        }
    }

    fun getTimeIn_12_Hours(activity: Context, time: String): String {
        var outTime = ""
        var AM_PM = ""
        val timeSplitting = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var inhours = Integer.parseInt(timeSplitting[0])
        val inmin = Integer.parseInt(timeSplitting[1])
        if (inhours >= 12) {
            AM_PM = " " + activity.resources.getString(R.string.pm)
            if (inhours != 12)
                inhours = inhours - 12
        } else {
            if (inhours == 0)
                inhours = 12
            AM_PM = " " + activity.resources.getString(R.string.am)
        }

        if (inmin < 10) {
            // 23:03
            outTime = (inhours.toString() + ":" + "0"
                    + inmin.toString() + AM_PM)
        } else {
            outTime = (inhours.toString() + ":" + inmin.toString()
                    + AM_PM)
        }
        return outTime
    }

    fun getRelativeDateString(activity: AppCompatActivity, TimeInMillisec: String): String {
        var date = ""
        val time = java.lang.Long.parseLong(TimeInMillisec) * 1000
        //check if time is today or yesterday
        val todaycal = Calendar.getInstance() // today
        val timecal = Calendar.getInstance()
        timecal.timeInMillis = time
        if (todaycal.get(Calendar.YEAR) == timecal.get(Calendar.YEAR) && todaycal.get(Calendar.DAY_OF_YEAR) == timecal.get(
                Calendar.DAY_OF_YEAR
            )
        ) {
            return activity.resources.getString(R.string.today)
        }
        todaycal.add(Calendar.DAY_OF_YEAR, -1) // yesterday
        if (todaycal.get(Calendar.YEAR) == timecal.get(Calendar.YEAR) && todaycal.get(Calendar.DAY_OF_YEAR) == timecal.get(
                Calendar.DAY_OF_YEAR
            )
        ) {
            return activity.resources.getString(R.string.yesterday)
        }
        // Date calendar = new Date(time);
        val formatter = SimpleDateFormat("yyyy MMM dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val ret = formatter.format(calendar.time)
        val datesinSpace = ret.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val year = Integer.parseInt(datesinSpace[0])
        val mcurrentDate = Calendar.getInstance()
        val nYear = mcurrentDate.get(Calendar.YEAR)
        if (nYear == year) {
            if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                //arabic //mmm dd
                date = datesinSpace[1] + " " + datesinSpace[2]
            } else {
                //dd mmm
                date = datesinSpace[2] + " " + datesinSpace[1]
            }
        } else {
            if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                //arabic //yyyy mmm dd
                date = (datesinSpace[0] + " " + datesinSpace[1] + " "
                        + datesinSpace[2])
            } else {
                //dd mmm yyyy
                date = (datesinSpace[2] + " " + datesinSpace[1] + " "
                        + datesinSpace[0])
            }
        }
        return date
    }

    fun getRelativeTimeSpanString(
        activity: AppCompatActivity,
        TimeInMillisec: String, DiffrenceBtweenDeviceAndServer: String
    ): String {
        // ,String DiffrenceBtweenDeviceAndServer
        val currentTime =
            System.currentTimeMillis() + java.lang.Long.parseLong(DiffrenceBtweenDeviceAndServer)
        val time = java.lang.Long.parseLong(TimeInMillisec) * 1000

        if (currentTime > time) {
            // in past
            val diff_Time = currentTime - time
            // if less than 10 sec
            val Time_in_Secends = diff_Time / 1000
            if (Time_in_Secends <= 10) {
                return activity.resources.getString(R.string.just_now)
            }

            if (Time_in_Secends < 60) {
                return if (Time_in_Secends.toInt() == 1) {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        (activity.resources.getString(R.string.ago)
                                + " "
                                + Time_in_Secends.toInt()
                                + " "
                                + activity.resources.getString(
                            R.string.second
                        ))
                    } else {
                        (Time_in_Secends.toInt().toString()
                                + " "
                                + activity.resources.getString(
                            R.string.second
                        )
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }

                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_Secends.toInt() < 11 && Time_in_Secends.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_Secends.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.seconds
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_Secends.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.second
                            ))
                        }

                    } else {
                        (Time_in_Secends.toInt().toString()
                                + " "
                                + activity.resources.getString(
                            R.string.seconds
                        )
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }
                }

            }
            // if less than 1 hour
            val Time_in_min = Time_in_Secends / 60
            if (Time_in_min < 60) {
                return if (Time_in_min.toInt() == 1) {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        (activity.resources.getString(R.string.ago)
                                + " "
                                + Time_in_min.toInt()
                                + " "
                                + activity.resources.getString(
                            R.string.min
                        ))
                    } else {
                        (Time_in_min.toInt().toString()
                                + " "
                                + activity.resources.getString(
                            R.string.min
                        )
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }
                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_min.toInt() < 11 && Time_in_min.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_min.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.mins
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_min.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.min
                            ))
                        }
                    } else {
                        (Time_in_min.toInt().toString()
                                + " "
                                + activity.resources.getString(
                            R.string.mins
                        )
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }
                }

            }
            // if less than 1 day
            val Time_in_hours = Time_in_min / 60
            if (Time_in_hours < 24) {
                return if (Time_in_hours.toInt() == 1) {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        (activity.resources.getString(R.string.ago)
                                + " "
                                + Time_in_hours.toInt()
                                + " "
                                + activity.resources
                            .getString(R.string.hr))
                    } else {
                        (Time_in_hours.toInt().toString()
                                + " "
                                + activity.resources
                            .getString(R.string.hr)
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }
                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_hours.toInt() < 11 && Time_in_hours.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_hours.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.hrs
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.ago
                            )
                                    + " "
                                    + Time_in_hours.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.hr
                            ))
                        }
                    } else {
                        (Time_in_hours.toInt().toString()
                                + " "
                                + activity.resources.getString(
                            R.string.hrs
                        )
                                + " "
                                + activity.resources.getString(
                            R.string.ago
                        ))
                    }
                }

            }
            // if less than 2 day
            val Time_in_days = Time_in_hours / 24
            if (Time_in_days == 1L) {
                return activity.resources.getString(R.string.yesterday)
            }

            if (Time_in_days == 2L) {
                return activity.resources.getString(R.string._2_days_ago)
            }

            if (Time_in_days == 3L) {
                return activity.resources.getString(R.string._3_days_ago)
            }

            if (Time_in_days == 4L) {
                return activity.resources.getString(R.string._4_days_ago)
            }

            var DateToShow = ""
            val date = getRelativeDateString(activity, TimeInMillisec)
            val time_string = getTime(activity, TimeInMillisec)
            DateToShow = "$date , $time_string"
            return DateToShow
        } else {
            val diff_Time = time - currentTime
            // if now
            if (diff_Time == 0L) {
                return activity.resources.getString(R.string.just_now)
            }
            // if less than 50 sec
            val Time_in_Secends = diff_Time / 1000
            if (Time_in_Secends <= 50) {
                return activity.resources.getString(R.string.just_now)
            }

            if (Time_in_Secends < 60) {
                return if (Time_in_Secends.toInt() == 1) {
                    (activity.resources.getString(R.string.`in`)
                            + " "
                            + Time_in_Secends.toInt()
                            + " "
                            + activity.resources
                        .getString(R.string.second))
                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_Secends.toInt() < 11 && Time_in_Secends.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_Secends.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.seconds
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_Secends.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.second
                            ))
                        }
                    } else {
                        (activity.resources.getString(R.string.`in`)
                                + " "
                                + Time_in_Secends.toInt()
                                + " "
                                + activity.resources.getString(
                            R.string.seconds
                        ))
                    }
                }

            }
            // if less than 1 hour
            val Time_in_min = Time_in_Secends / 60
            if (Time_in_min < 60) {
                return if (Time_in_min.toInt() == 1) {
                    (activity.resources.getString(R.string.`in`)
                            + " "
                            + Time_in_min.toInt()
                            + " "
                            + activity.resources
                        .getString(R.string.minute))
                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_min.toInt() < 11 && Time_in_min.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_min.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.minutes
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_min.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.minute
                            ))
                        }

                    } else {
                        (activity.resources.getString(R.string.`in`)
                                + " "
                                + Time_in_min.toInt()
                                + " "
                                + activity.resources.getString(
                            R.string.minutes
                        ))
                    }
                }

            }
            // if less than 1 day
            val Time_in_hours = Time_in_min / 60
            if (Time_in_hours < 24) {
                return if (Time_in_hours.toInt() == 1) {
                    (activity.resources.getString(R.string.`in`) + " "
                            + Time_in_hours.toInt() + " "
                            + activity.resources.getString(R.string.hour))
                } else {
                    if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
                        if (Time_in_hours.toInt() < 11 && Time_in_hours.toInt() > 2) {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_hours.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.hours
                            ))
                        } else {
                            (activity.resources.getString(
                                R.string.`in`
                            )
                                    + " "
                                    + Time_in_hours.toInt()
                                    + " "
                                    + activity.resources.getString(
                                R.string.hour
                            ))
                        }
                    } else {
                        (activity.resources.getString(R.string.`in`)
                                + " "
                                + Time_in_hours.toInt()
                                + " "
                                + activity.resources.getString(
                            R.string.hours
                        ))
                    }
                }

            }
            // if less than 2 day
            val Time_in_days = Time_in_hours / 24
            if (Time_in_days == 1L) {
                return activity.resources.getString(R.string.tomorrow)
            }

            if (Time_in_days == 2L) {
                return activity.resources.getString(R.string.in_2_days)
            }

            if (Time_in_days == 3L) {
                return activity.resources.getString(R.string.in_3_days)
            }

            if (Time_in_days == 4L) {
                return activity.resources.getString(R.string.in_4_days)
            }

            var DateToShow = ""
            val date = getRelativeDateString(activity, TimeInMillisec)
            val time_string = getTime(activity, TimeInMillisec)
            DateToShow = "$date , $time_string"
            return DateToShow
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(activity: AppCompatActivity, milliSec: String): String {
        val time = java.lang.Long.parseLong(milliSec) * 1000
        val formatter = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val ret = formatter.format(calendar.time)
        return getTimeIn_12_Hours(activity, ret)
    }

    fun getDateOrTimeToday(activity: AppCompatActivity, milliSec: String): String {
        val time = java.lang.Long.parseLong(milliSec) * 1000
        return if (DateUtils.isToday(time)) {
            //if to day return time only
            getTime(activity, milliSec)
        } else {
            //return date
            getRelativeDateString(activity, milliSec)
        }
    }

    fun getTimeIn_24_Hours(activity: Context, time: String): String {
        try {
            val timeSplitting =
                time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (timeSplitting[1] == activity.resources.getString(R.string.pm)) {
                val time_second_Splitting =
                    timeSplitting[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                var inhours = Integer.parseInt(time_second_Splitting[0])
                if (inhours == 12)
                    inhours = 12
                else
                    inhours = inhours + 12
                val inmin = Integer.parseInt(time_second_Splitting[1])
                return inhours.toString() + ":" + inmin
            } else {
                val time_second_Splitting =
                    timeSplitting[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                var inhours = Integer.parseInt(time_second_Splitting[0])
                if (inhours == 12)
                    inhours = 0
                val inmin = Integer.parseInt(time_second_Splitting[1])
                return inhours.toString() + ":" + inmin
            }
        } catch (e: Exception) {
            return time
        }

    }

    fun getTimeMilliInMins(milliSeconds: Long): String {
        val mins = milliSeconds.toInt() / 1000 / 60
        val secs = milliSeconds / 1000 - mins * 60
        var minitues = ""
        if (mins < 10) {
            minitues = "0$mins"
        } else
            minitues = "" + mins
        var seconds = ""
        if (secs < 10) {
            seconds = "0$secs"
        } else
            seconds = "" + secs
        return "$minitues:$seconds"

        //        if(mins<60){
        //
        //        }else if(mins=60){
        //
        //        }
        //        if (milliSeconds > 1) {
        //            return mins + " " + activity.getResources().getString(R.string.mins);
        //        } else {
        //            return mins + " " + activity.getResources().getString(R.string.min);
        //        }
    }

    fun getTimeInHoursMins(
        activity: androidx.fragment.app.FragmentActivity,
        TimeInMins: String
    ): String {
        try {
            val time = java.lang.Double.parseDouble(TimeInMins)
            if (time <= 1)
                return 1.toString() + " " + activity.resources.getString(R.string.min)
            else if (time < 60)
                return time.toInt().toString() + " " + activity.resources.getString(R.string.mins)
            else if (time == 60.0)
                return 1.toString() + " " + activity.resources.getString(R.string.hr)
            else {
                val hours = time.toInt() / 60
                val mins = time.toInt() % 60
                var result = ""
                if (hours == 1)
                    result = result + hours + " " + activity.resources.getString(R.string.hr)
                else
                    result = result + hours + " " + activity.resources.getString(R.string.hrs)
                if (mins == 1)
                    result = result + " " + mins + " " + activity.resources.getString(R.string.min)
                else
                    result = result + " " + mins + " " + activity.resources.getString(R.string.mins)
                return result
            }
        } catch (e: Exception) {
            return 50.toString() + " " + activity.resources.getString(R.string.mins)
        }

    }

    fun getDateInMonthName(date: String, activity: Context): String {
        try {
            val months = activity.resources.getStringArray(R.array.months_symbols)
            val dateArr = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val _calendar = Calendar.getInstance(Locale.getDefault())
            val year = _calendar.get(Calendar.YEAR)

            return dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0]
            //            if (Preferences.getInstance().getApplicationLocale() != null &&
            //                    Preferences.getInstance().getApplicationLocale().equals("en")) {
            //                return dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0];
            //            } else {
            //                return dateArr[2] + "\\" + dateArr[1] + "\\" + dateArr[0];
            //            }
            //            String newDatFrom;
            //            if (year == Integer.parseInt(dateArr[0])) {
            //                return dateArr[2] + " " + months[Integer.parseInt(dateArr[1]) - 1];
            //            } else {
            //                return dateArr[2] + " " + months[Integer.parseInt(dateArr[1]) - 1] + " " + dateArr[0];
            //            }
        } catch (e: Exception) {
            return ""
        }

    }
}