package com.revature.qea;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** Runs only the scenarios tagged @smoke. Settings: junit-platform.properties. */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@IncludeTags("smoke")
public class RunSmokeTest {
}
