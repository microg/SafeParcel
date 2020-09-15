/*
 * SPDX-FileCopyrightText: 2019, microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.safeparcel.test.auto;

import org.microg.safeparcel.SafeParcelable;

public enum Type {
    @SafeParcelable.Field(1)
    ONLINE,
    @SafeParcelable.Field(2)
    OFFLINE
}
