package com.epam.pipeline.tesadapter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;


@ApiModel(description = "Input describes Task input files.")
@Data
public class TesInput {

    @ApiModelProperty(value = "")
    @JsonProperty("name")
    private String name = null;

    @ApiModelProperty(value = "")
    @JsonProperty("description")
    private String description = null;

    @ApiModelProperty(value = "REQUIRED, unless \"content\" is set.  URL in long term storage, for example: " +
            "s3://my-object-store/file1 gs://my-bucket/file2 file:///path/to/my/file /path/to/my/file etc...")
    @JsonProperty("url")
    private String url = null;

    @ApiModelProperty(value = "Path of the file inside the container. Must be an absolute path.")
    @JsonProperty("path")
    private String path = null;

    @ApiModelProperty(value = "Type of the file, FILE or DIRECTORY")
    @Valid
    @JsonProperty("type")
    private TesFileType type = null;

    @ApiModelProperty(value = "File content literal.  Implementations should support a minimum of 128 KiB " +
            "in this field and may define its own maximum. UTF-8 encoded  If content is not empty, \"url\" " +
            "must be ignored.")
    @JsonProperty("content")
    private String content = null;
}

