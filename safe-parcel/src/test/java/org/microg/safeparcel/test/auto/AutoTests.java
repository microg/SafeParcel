package org.microg.safeparcel.test.auto;

import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Test;
import org.microg.safeparcel.test.mock.MockParcel;

import static org.junit.Assert.assertEquals;

public class AutoTests {
    static <T extends Parcelable> T remarshal(T orig, Parcelable.Creator<T> tCreator) {
        Parcel parcel = MockParcel.obtain();
        orig.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        parcel = MockParcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        T re = tCreator.createFromParcel(parcel);
        parcel.recycle();
        return re;
    }

    @Test
    public void bar() {
        Bar bar1 = new Bar(12);
        Bar bar2 = remarshal(bar1, Bar.CREATOR);
        assertEquals(bar1, bar2);
    }

    @Test
    public void foo() {
        Foo foo1 = new Foo(4);
        foo1.string = "Hello";
        foo1.stringList.add("Hello2");
        foo1.stringStringMap.put("Hello3", "Hello4");
        foo1.bar = new Bar(5);
        foo1.barList.add(foo1.bar);
        foo1.barArray = new Bar[]{foo1.bar};
        foo1.intList.add(2);
        Foo foo2 = remarshal(foo1, Foo.CREATOR);
        assertEquals(foo1, foo2);
    }
}

