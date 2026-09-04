/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.notifications.airship

/**
 * Attribute values supported by Lantern's Airship audience helper.
 */
sealed class AirshipAudienceAttributeValue {
    /**
     * String audience attribute value.
     */
    class StringValue(val value: String) : AirshipAudienceAttributeValue()

    /**
     * Integer audience attribute value.
     */
    class IntValue(val value: Int) : AirshipAudienceAttributeValue()

    /**
     * Long audience attribute value.
     */
    class LongValue(val value: Long) : AirshipAudienceAttributeValue()

    /**
     * Float audience attribute value.
     */
    class FloatValue(val value: Float) : AirshipAudienceAttributeValue()

    /**
     * Double audience attribute value.
     */
    class DoubleValue(val value: Double) : AirshipAudienceAttributeValue()
}
