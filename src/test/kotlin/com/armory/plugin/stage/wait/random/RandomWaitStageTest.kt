package com.armory.plugin.stage.wait.random

import com.netflix.spinnaker.orca.api.SimpleStageInput
import com.netflix.spinnaker.orca.api.SimpleStageOutput
import com.netflix.spinnaker.orca.api.SimpleStageStatus
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class RandomWaitStageTest : JUnit5Minutests {

    fun tests() = rootContext {
        test("execute random wait stage") {
            expectThat(RandomWaitStage().execute(SimpleStageInput(RandomWaitInput(1))))
                    .isEqualTo(
                            SimpleStageOutput<Output, Context>().apply {
                                status = SimpleStageStatus.SUCCEEDED
                                output = Output(0)
                                context = Context(1)
                            }
                    )
        }
    }
}