package com.matin.core.data

import org.junit.Assert.*
import org.junit.Test

class NetworkMapperTest {

    @Test
    fun `toDomain should map NetworkPlayers to Players`() {

        assertEquals(fakeDomainPlayers, fakeNetworkPlayers.toDomain())
    }
}