package com.google.api.apichaining;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
// Test Class to Run UI Automation and API Automation
@RunWith(Cucumber.class)
@CucumberOptions(features="features",glue="com.google.api.apichaining")
public class AutomationRunner {

}
