package com.example.busapp

import android.os.Parcel
import android.os.Parcelable

data class Sefer(
    val firma: String = "",
    val tarih: String = "",
    val saat: String = "",
    val fiyat: String = "",
    val kalan_koltuk: Int = 0 // kalan_koltuk'u Int olarak tanımla
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() // kalan_koltuk'u Int olarak oku
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firma)
        parcel.writeString(tarih)
        parcel.writeString(saat)
        parcel.writeString(fiyat)
        parcel.writeInt(kalan_koltuk) // kalan_koltuk'u Int olarak yaz
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sefer> {
        override fun createFromParcel(parcel: Parcel): Sefer {
            return Sefer(parcel)
        }

        override fun newArray(size: Int): Array<Sefer?> {
            return arrayOfNulls(size)
        }
    }
}
