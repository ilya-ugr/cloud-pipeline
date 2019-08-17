package com.epam.pipeline.tesadapter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Output describes Task output files.
 */
@ApiModel(description = "Output describes Task output files.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-17T00:22:00.237+03:00")

public class TesOutput   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("path")
  private String path = null;

  @JsonProperty("type")
  private TesFileType type = null;

  public TesOutput name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TesOutput description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TesOutput url(String url) {
    this.url = url;
    return this;
  }

  /**
   * URL in long term storage, for example: s3://my-object-store/file1 gs://my-bucket/file2 file:///path/to/my/file /path/to/my/file etc...
   * @return url
  **/
  @ApiModelProperty(value = "URL in long term storage, for example: s3://my-object-store/file1 gs://my-bucket/file2 file:///path/to/my/file /path/to/my/file etc...")


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public TesOutput path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Path of the file inside the container. Must be an absolute path.
   * @return path
  **/
  @ApiModelProperty(value = "Path of the file inside the container. Must be an absolute path.")


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public TesOutput type(TesFileType type) {
    this.type = type;
    return this;
  }

  /**
   * Type of the file, FILE or DIRECTORY
   * @return type
  **/
  @ApiModelProperty(value = "Type of the file, FILE or DIRECTORY")

  @Valid

  public TesFileType getType() {
    return type;
  }

  public void setType(TesFileType type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TesOutput tesOutput = (TesOutput) o;
    return Objects.equals(this.name, tesOutput.name) &&
        Objects.equals(this.description, tesOutput.description) &&
        Objects.equals(this.url, tesOutput.url) &&
        Objects.equals(this.path, tesOutput.path) &&
        Objects.equals(this.type, tesOutput.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, url, path, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TesOutput {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

