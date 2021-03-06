/*
 *
 *  Copyright 2018 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package com.netflix.genie.agent.cli

import com.netflix.genie.agent.execution.statemachine.JobExecutionStateMachine
import com.netflix.genie.agent.execution.statemachine.States
import com.netflix.genie.test.categories.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class ExecCommandSpec extends Specification {
    ExecCommand.ExecCommandArguments args
    JobExecutionStateMachine stateMachine;

    void setup() {
        args = Mock()
        stateMachine = Mock()
    }

    void cleanup() {
    }

    def "Run"() {
        setup:
        def execCommand = new ExecCommand(args, stateMachine)

        when:
        execCommand.run()

        then:
        1 * stateMachine.start()
        1 * stateMachine.waitForStop() >> States.END
    }

    def "Run interrupted"() {
        setup:
        def execCommand = new ExecCommand(args, stateMachine)

        when:
        execCommand.run()

        then:
        1 * stateMachine.start()
        1 * stateMachine.waitForStop() >> { throw new InterruptedException() }

        thrown(RuntimeException.class)
    }

    def "Run fail"() {
        setup:
        def execCommand = new ExecCommand(args, stateMachine)

        when:
        execCommand.run()

        then:
        1 * stateMachine.start()
        1 * stateMachine.waitForStop() >> null

        thrown(RuntimeException.class)
    }
}
