package com.revature.qea;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** Runs every feature file except work in progress. Settings: junit-platform.properties. */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ExcludeTags("wip")
public class RunCucumberTest {
}