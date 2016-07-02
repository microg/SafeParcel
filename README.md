SafeParcel
==========

SafeParcel is a mechanism to serialize objects on the Android platform into Parcelables version agnostic.

This is achieved by prefixing each field with a unique identifying number and its length.
When deserializing, unknown fields can be skipped and the field order is not relevant.

The SafeParcel format was originally developed by Google for Play Services.
Until now, neither the format description, nor a library to use it had been released by Google.

For this reason, although we spent much effort into it, we can't guarantee that this implementation is 100% compatible
with Google's implementation.

Usage
-----

SafeParcel is in the Maven Central Repository. To use it, just add the following dependency statement to your
Android app or library:

    compile 'org.microg:safe-parcel:[version]'

To find the latest available version, [check the central repository](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.microg%22%20AND%20a%3A%22safe-parcel%22)

### Directly accessing SafeParcel format

The `SafeParcelReader` and `SafeParcelWriter` allow to directly read from or write to the SafeParcel format.
However, usage of them requires some amount of code to be written,
which is painful if you want to store many objects in SafeParcel format.
Therefor, there is a more automatic approach based on reflection build into the library

### Automatic safe parceling

The `SafeParcelUtil` provides several easy methods to use the SafeParcel format to serialize and deserialize object.
The three methods `createObject`, `readObject` and `writeObject` allow to create an object from parcel,
read from parcel into an existing object or write an object into a parcel.

To use these, you first need to annotate the fields in the object you want to parcel and make the object implement the SafeParcelable interface.
In most cases you simply add the `@SafeParceled` annotation to give each field a unique number.
When using ArrayLists, it is required to further annotate the field. Check documentation of `@SafeParceled` for details.
Additionally, make sure that the object has a default or no-parameter constructor.
If you don't want such a constructor to be part of the API, make the constructor private.

For convenience, you can even skip the work of calling the methods in `SafeParcelUtil` by extending the abstract
`AutoSafeParcelable` class. After that, the only thing required to unparcel the object is to add a static `CREATOR` field with an `AutoCreator` value, check the example below.

### Example

```java
import org.microg.safeparcel.*;

public class ExampleObject extends AutoSafeParcelable {
    @SafeParceled(1000)
    private int versionCode = 1;

    @SafeParceled(1)
    public String name;

    @SafeParceled(value=2, subClass=Integer.class)
    public List<Integer> ids = new ArrayList<Integer>();

    public static final Creator<ExampleObject> CREATOR = new AutoCreator<ExampleObject>(ExampleObject.class);
}
```

Note: When using ProGuard and automatic safe parceling, make sure that all relevant classes and
annotations are available at runtime, as SafeParcelUtil will use reflection. See `proguard.txt` for relevant proguard rules.

SafeParcel design patterns
--------------------------

It is recommended to add a version code to the fields being parceled in a SafeParcel object.
Although the SafeParcel format is version agnostic, your code maybe is not.  With a version code,
you can realize that the object was deserialized from an older version and ensure that fields that did not
exist previously are populated.

Additionally it turned out to be a good idea to not remove fields that have been used in the previous version of your
app or library. This way, code relying on the old version will continue to work. For performance reason or if it is an
unreasonable amount of work to manage the old code, you can still remove the field in a future version, after having
the field being marked as deprecated for some time.

License
-------

    Copyright 2014-2016 microG Project Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
