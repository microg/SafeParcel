/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.safeparcel;

import android.os.Parcel;

import java.lang.reflect.Array;

public abstract class AutoSafeParcelable implements SafeParcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        SafeParcelUtil.writeObject(this, out, flags);
    }

    public static class AutoCreator<T extends SafeParcelable> implements Creator<T> {

        private final Class<T> tClass;

        public AutoCreator(Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        public T createFromParcel(Parcel parcel) {
            return SafeParcelUtil.createObject(tClass, parcel);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T[] newArray(int i) {
            return (T[]) Array.newInstance(tClass, i);
        }
    }
}
