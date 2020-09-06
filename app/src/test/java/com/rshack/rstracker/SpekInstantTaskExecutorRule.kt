package com.rshack.rstracker

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor

/**
 * In order to test LiveData, the `InstantTaskExecutorRule` rule needs to be applied via JUnit.
 * As we are running it with Spek2, the "rule" will be implemented in this way instead
 * https://github.com/spekframework/spek/issues/337
 */

class SpekInstantTaskExecutorRule {
    companion object Arch {
        fun execute() {
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }
            })
        }
        fun finish() {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }
}
