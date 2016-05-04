package com.fmi.evelina.unimobileapp.model.election_camaign_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElectionCourse implements Parcelable {
    @Expose
    @SerializedName("id")
    public int Id;

    @Expose
    @SerializedName("name")
    public String Name;

    @Expose
    @SerializedName("description")
    public String Description;

    @Expose
    @SerializedName("hours")
    public String Hours;

    @Expose
    @SerializedName("lecturer")
    public String Lecturer;

    @Expose
    @SerializedName("has_free_spaces")
    public Boolean HasFreeSpaces;

    @Expose
    @SerializedName("is_for_my_program")
    public Boolean IsForMyProgram;

    @Expose
    @SerializedName("is_for_my_grade")
    public Boolean IsForMyGrade;

    @Expose
    @SerializedName("is_enrolled")
    public Boolean Enrolled;

    @Expose
    @SerializedName("category")
    public String Category;

    @Expose
    @SerializedName("credits")
    public Double Credits;

    @Expose
    @SerializedName("min_grade")
    public Integer MinGrade;

    public  ElectionCourse() {}

    protected ElectionCourse(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Description = in.readString();
        Hours = in.readString();
        Lecturer = in.readString();
        byte HasFreeSpacesVal = in.readByte();
        HasFreeSpaces = HasFreeSpacesVal == 0x02 ? null : HasFreeSpacesVal != 0x00;
        byte IsForMyProgramVal = in.readByte();
        IsForMyProgram = IsForMyProgramVal == 0x02 ? null : IsForMyProgramVal != 0x00;
        byte IsForMyGradeVal = in.readByte();
        IsForMyGrade = IsForMyGradeVal == 0x02 ? null : IsForMyGradeVal != 0x00;
        byte EnrolledVal = in.readByte();
        Enrolled = EnrolledVal == 0x02 ? null : EnrolledVal != 0x00;
        Category = in.readString();
        Credits = in.readByte() == 0x00 ? null : in.readDouble();
        MinGrade = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Description);
        dest.writeString(Hours);
        dest.writeString(Lecturer);
        if (HasFreeSpaces == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (HasFreeSpaces ? 0x01 : 0x00));
        }
        if (IsForMyProgram == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (IsForMyProgram ? 0x01 : 0x00));
        }
        if (IsForMyGrade == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (IsForMyGrade ? 0x01 : 0x00));
        }
        if (Enrolled == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (Enrolled ? 0x01 : 0x00));
        }
        dest.writeString(Category);
        if (Credits == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(Credits);
        }
        dest.writeInt(MinGrade);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ElectionCourse> CREATOR = new Parcelable.Creator<ElectionCourse>() {
        @Override
        public ElectionCourse createFromParcel(Parcel in) {
            return new ElectionCourse(in);
        }

        @Override
        public ElectionCourse[] newArray(int size) {
            return new ElectionCourse[size];
        }
    };
}
