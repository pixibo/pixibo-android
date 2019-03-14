package com.pomelo.pixibo.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultModel  implements Parcelable {


    private boolean recommended;
    private String size,bust,waist,hip,bust8Bit,waist8Bit,hip8Bit,confidence;

    public  ResultModel()
    {}

    public ResultModel(boolean recommended , String size , String bust , String waist , String hip, String bust8Bit , String waist8Bit , String hip8Bit, String confidence)
    {
        this.recommended = recommended;
        this.size = size;
        this.bust = bust;
        this.waist = waist;
        this.hip = hip;
        this.bust8Bit = bust8Bit;
        this.waist8Bit = waist8Bit;
        this.hip8Bit = hip8Bit;
        this.confidence = confidence;

    }

    protected ResultModel(Parcel in) {
        recommended = in.readByte() != 0;
        size = in.readString();
        bust = in.readString();
        waist = in.readString();
        hip = in.readString();
        bust8Bit = in.readString();
        waist8Bit = in.readString();
        hip8Bit = in.readString();
        confidence = in.readString();
    }

    public static final Creator<ResultModel> CREATOR = new Creator<ResultModel>() {
        @Override
        public ResultModel createFromParcel(Parcel in) {
            return new ResultModel(in);
        }

        @Override
        public ResultModel[] newArray(int size) {
            return new ResultModel[size];
        }
    };

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBust() {
        return bust;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHip() {
        return hip;
    }

    public void setHip(String hip) {
        this.hip = hip;
    }

    public String getBust8Bit() {
        return bust8Bit;
    }

    public void setBust8Bit(String bust8Bit) {
        this.bust8Bit = bust8Bit;
    }

    public String getWaist8Bit() {
        return waist8Bit;
    }

    public void setWaist8Bit(String waist8Bit) {
        this.waist8Bit = waist8Bit;
    }

    public String getHip8Bit() {
        return hip8Bit;
    }

    public void setHip8Bit(String hip8Bit) {
        this.hip8Bit = hip8Bit;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (recommended ? 1 : 0));
        dest.writeString(size);
        dest.writeString(bust);
        dest.writeString(waist);
        dest.writeString(hip);
        dest.writeString(bust8Bit);
        dest.writeString(waist8Bit);
        dest.writeString(hip8Bit);
        dest.writeString(confidence);
    }
}



