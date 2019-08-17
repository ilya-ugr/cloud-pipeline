package com.epam.pipeline.tesadapter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ExecutorLog describes logging information related to an Executor.
 */
@ApiModel(description = "ExecutorLog describes logging information related to an Executor.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-17T00:22:00.237+03:00")

public class TesExecutorLog   {
  @JsonProperty("start_time")
  private String startTime = null;

  @JsonProperty("end_time")
  private String endTime = null;

  @JsonProperty("stdout")
  private String stdout = null;

  @JsonProperty("stderr")
  private String stderr = null;

  @JsonProperty("exit_code")
  private Integer exitCode = null;

  public TesExecutorLog startTime(String startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Time the executor started, in RFC 3339 format.
   * @return startTime
  **/
  @ApiModelProperty(value = "Time the executor started, in RFC 3339 format.")


  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public TesExecutorLog endTime(String endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * Time the executor ended, in RFC 3339 format.
   * @return endTime
  **/
  @ApiModelProperty(value = "Time the executor ended, in RFC 3339 format.")


  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public TesExecutorLog stdout(String stdout) {
    this.stdout = stdout;
    return this;
  }

  /**
   * Stdout content.  This is meant for convenience. No guarantees are made about the content. Implementations may chose different approaches: only the head, only the tail, a URL reference only, etc.  In order to capture the full stdout users should set Executor.stdout to a container file path, and use Task.outputs to upload that file to permanent storage.
   * @return stdout
  **/
  @ApiModelProperty(value = "Stdout content.  This is meant for convenience. No guarantees are made about the content. Implementations may chose different approaches: only the head, only the tail, a URL reference only, etc.  In order to capture the full stdout users should set Executor.stdout to a container file path, and use Task.outputs to upload that file to permanent storage.")


  public String getStdout() {
    return stdout;
  }

  public void setStdout(String stdout) {
    this.stdout = stdout;
  }

  public TesExecutorLog stderr(String stderr) {
    this.stderr = stderr;
    return this;
  }

  /**
   * Stderr content.  This is meant for convenience. No guarantees are made about the content. Implementations may chose different approaches: only the head, only the tail, a URL reference only, etc.  In order to capture the full stderr users should set Executor.stderr to a container file path, and use Task.outputs to upload that file to permanent storage.
   * @return stderr
  **/
  @ApiModelProperty(value = "Stderr content.  This is meant for convenience. No guarantees are made about the content. Implementations may chose different approaches: only the head, only the tail, a URL reference only, etc.  In order to capture the full stderr users should set Executor.stderr to a container file path, and use Task.outputs to upload that file to permanent storage.")


  public String getStderr() {
    return stderr;
  }

  public void setStderr(String stderr) {
    this.stderr = stderr;
  }

  public TesExecutorLog exitCode(Integer exitCode) {
    this.exitCode = exitCode;
    return this;
  }

  /**
   * Exit code.
   * @return exitCode
  **/
  @ApiModelProperty(value = "Exit code.")


  public Integer getExitCode() {
    return exitCode;
  }

  public void setExitCode(Integer exitCode) {
    this.exitCode = exitCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TesExecutorLog tesExecutorLog = (TesExecutorLog) o;
    return Objects.equals(this.startTime, tesExecutorLog.startTime) &&
        Objects.equals(this.endTime, tesExecutorLog.endTime) &&
        Objects.equals(this.stdout, tesExecutorLog.stdout) &&
        Objects.equals(this.stderr, tesExecutorLog.stderr) &&
        Objects.equals(this.exitCode, tesExecutorLog.exitCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTime, endTime, stdout, stderr, exitCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TesExecutorLog {\n");
    
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    stdout: ").append(toIndentedString(stdout)).append("\n");
    sb.append("    stderr: ").append(toIndentedString(stderr)).append("\n");
    sb.append("    exitCode: ").append(toIndentedString(exitCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

