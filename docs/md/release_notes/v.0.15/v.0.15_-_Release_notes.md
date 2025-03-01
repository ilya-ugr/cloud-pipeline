# Cloud Pipeline v.0.15 - Release notes

- [Microsoft Azure Support](#microsoft-azure-support)
- [Notifications on the RAM/Disk pressure](#notifications-on-the-ramdisk-pressure)
- [Limit mounted storages](#limit-mounted-storages)
- [Personal SSH keys configuration](#personal-ssh-keys-configuration)
- [Allow to set the Grid Engine capability for the "fixed" cluster](#allow-to-set-the-grid-engine-capability-for-the-fixed-cluster)
- [Consider Cloud Providers' resource limitations when scheduling a job](#consider-cloud-providers-resource-limitations-when-scheduling-a-job)
- [Allow to terminate paused runs](#allow-to-terminate-paused-runs)
- [Pre/Post-commit hooks implementation](#prepost-commit-hooks-implementation)
- [Restricting manual installation of the nvidia tools](#restricting-manual-installation-of-the-nvidia-tools)
- [Setup swap files for the Cloud VMs](#setup-swap-files-for-the-cloud-vms)
- [Run's system paths shall be available for the general user account](#runs-system-paths-shall-be-available-for-the-general-user-account)
- [Renewed WDL visualization](#renewed-wdl-visualization)
- ["QUEUED" state of the run](#queued-state-of-the-run)
- [Help tooltips for the run state icons](#help-tooltips-for-the-run-state-icons)
- [VM monitor service](#vm-monitor-service)
- [Web GUI caching](#web-gui-caching)
- [Installation via pipectl](#installation-via-pipectl)
- [Add more logging to troubleshoot unexpected pods failures](#add-more-logging-to-troubleshoot-unexpected-pods-failures)
- [Displaying information on the nested runs](#displaying-information-on-the-nested-runs-within-a-parent-log-form)
- [`pipe` CLI warnings on the JWT expiration](#pipe-cli-warnings-on-the-jwt-expiration)
- [`pipe` configuration for using NTLM Authentication Proxy](#pipe-configuration-for-using-ntlm-authentication-proxy)
- [Environment Modules support](#environment-modules-support-for-the-cloud-pipeline-runs)
- [Sharing SSH access to running instances with other user(s)/group(s)](#sharing-ssh-access-to-running-instances-with-other-usersgroups)
- [Verification of docker/storage permissions when launching a run](#verification-of-dockerstorage-permissions-when-launching-a-run)

***

- [Notable Bug fixes](#notable-bug-fixes)
    - [Incorrect behavior of the global search filter](#incorrect-behavior-of-the-global-search-filter)
    - ["COMMITING..." status hangs](#commiting-status-hangs)
    - [Instances of Metadata entity aren't correctly sorted](#instances-of-metadata-entity-arent-correctly-sorted)
    - [Tool group cannot be deleted until all child tools are removed](#tool-group-cannot-be-deleted-until-all-child-tools-are-removed)
    - [Missing region while estimating a run price](#missing-region-while-estimating-a-run-price)
    - [Cannot specify region when an existing object storage is added](#cannot-specify-region-when-an-existing-object-storage-is-added)
    - [ACL control for PIPELINE_USER and ROLE entities for metadata API](#acl-control-for-pipeline_user-and-role-entities-for-metadata-api)
    - [Getting logs from Kubernetes may cause `OutOfMemory` error](#getting-logs-from-kubernetes-may-cause-outofmemory-error)
    - [Layout is broken for pipeline's versions table](#layout-is-broken-for-pipelines-versions-table)
    - [AWS: Incorrect `nodeup` handling of spot request status](#aws-incorrect-nodeup-handling-of-spot-request-status)
    - [Not handling clusters in `autopause` daemon](#not-handling-clusters-in-autopause-daemon)
    - [Incorrect `pipe` CLI version displaying](#incorrect-pipe-cli-version-displaying)

***

## Microsoft Azure Support

One of the major **`v0.15`** features is a support for the **[Microsoft Azure Cloud](https://azure.microsoft.com/en-us/)**.

All the features, that were previously used for `AWS`, are now available in all the same manner, from all the same GUI/CLI, for `Azure`.

Another cool thing, is that now it is possible to have a single installation of the `Cloud Pipeline`, which will manage both Cloud Providers (`AWS` and `Azure`). This provides a flexibility to launch jobs in the locations, closer to the data, with cheaper prices or better compute hardware for a specific task.

## Notifications on the RAM/Disk pressure

When a compute-intensive job is run - compute node may start starving for the resources.  
CPU high load is typically a normal situation - it could result just to the SSH/metrics slowdown. But running low on memory and disk could crash the node, in such cases autoscaler will eventually terminate the cloud instance.

In **`v0.15`** version, the `Cloud Pipeline` platform could notify user on the fact of Memory/Disk high load.  
When memory or disk consuming will be higher than a threshold value for a specified period of time (in average) - a notification will be sent (and resent after a delay, if the problem is still in place).

Such notifications could be configured at `HIGH_CONSUMED_RESOURCES` section of the `Email notifications`:  

![CP_v.0.15_ReleaseNotes](attachments/RN015_NotificationsResourcePressure_1.png)

The following items at `System` section of the `Preferences` define behavior of such notifications:

- **`system.memory.consume.threshold`** - memory threshold (in %) above which the notification would be sent
- **`system.disk.consume.threshold`** - disk threshold (in %) above which the notification would be sent
- **`system.monitoring.time.range`** - time delay (in sec) after which the notification would be sent again, if the problem is still in place.

See more information about [Email notifications](../../manual/12_Manage_Settings/12._Manage_Settings.md#email-notifications) and [System Preferences](../../manual/12_Manage_Settings/12.10._Manage_system-level_settings.md#system).

## Limit mounted storages

Previously, all available storages were mounted to the container during the run initialization. User could have access to them via `/cloud-data` or `~/cloud-data` folder using the interactive sessions (SSH/Web endpoints/Desktop) or `pipeline` runs.

For certain reasons (e.g. takes too much time to mount all or a run is going to be shared with others), user may want to limit the number of data storages being mounted to a specific job run.

Now, user can configure the list of the storages that will be mounted. This can be accomplished using the `Limit mount` field of the `Launch` form:

- By default, `All available storages` are mounted (i.e. the ones, that user has `ro` or `rw` permissions)
- To change the default behavior - click the drop-down list next to "Limit mounts" label:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_LimitMountStorage_1.png)
- Select storages that shall be mounted:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_LimitMountStorage_2.png)
- Review that only a limited number of data storages is mounted:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_LimitMountStorage_3.png)
- Mounted storage is available for the interactive/batch jobs using the path `/cloud-data/{storage_name}`:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_LimitMountStorage_4.png)

See an example [here](../../manual/06_Manage_Pipeline/6.1._Create_and_configure_pipeline.md#example-limit-mounted-storages).

## Personal SSH keys configuration

Previously, there were two options to communicate with the embedded gitlab repositories, that host pipelines code:

- From the local (non-cloud) machine: use the `https` protocol and the repository URI, provided by the GUI (i.e. `GIT REPOSITORY` button in the pipeline view)
- From the cloud compute node: `git` command line interface, that is preconfigured to authenticate using https protocol and the auto-generated credentials

Both options consider that https is used. It worked fine for 99% of the use cases. But for some of the applications - `ssh` protocol is required as this is the only way to achieve a passwordless authentication against the gitlab instance

To address this issue, SSH keys management was introduced:

- Users' ssh keys are generated by the `Git Sync` service
- SSH key-pair is created and assigned to the `Cloud Pipeline` user and a public key is registered in the GitLab
- Those SSH keys are now also configured for all the runs, launched by the user. So it is possible to perform a passwordless authentication, when working with the gitlab or other services, that will be implemented in the near future
- HTTPS/SSH selector is added to the `GIT REPOSITORY` popup of the `Cloud Pipeline` Web GUI:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SSHKeysConfig_1.png)
- Default selection is HTTP, which displays the same URI as previously (repository), but user is able to switch it to the SSH and get the reformatted address:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SSHKeysConfig_2.png)

For more details see [here](../../manual/06_Manage_Pipeline/6._Manage_Pipeline.md#details-controls).

## Allow to set the Grid Engine capability for the "fixed" cluster

Version `0.14` introduced an ability to launch `autoscaled` clusters. Besides the autoscaling itself - such cluster were configured to use `GridEngine` server by default, which is pretty handy.

On the other hand - `fixed` cluster (i.e. those which contain a predefined number of compute nodes), required user to set the `CP_CAP_SGE` explicitly. Which is no a big deal, but may be tedious.

In **`v0.15`** Grid Engine can be configured within the `Cluster` (fixed size cluster) tab.

This is accomplished by using the `Enable GridEngine` checkbox. By default, this checkbox is unticked. If the user sets this ON - `CP_CAP_SGE` parameter is added automatically.

Also a number of help icons is added to the `Cluster configuration` dialog to clarify the controls purpose:

- Popup header (E.g. next to the tabs line) - displays information on different cluster modes
- (Cluster) `Enable GridEngine checkbox` - displays information on the GridEngine usage
- (Auto-scaled cluster) `Auto-scaled up` - displays information on the autoscaling logic
- (Auto-scaled cluster) `Default child nodes` - displays information on the initial node pool size

![CP_v.0.15_ReleaseNotes](attachments/RN015_GE_Autoconfig_1.png)

See more information about cluster launch [here](../../manual/06_Manage_Pipeline/6._Manage_Pipeline.md#configuration).

## Consider Cloud Providers' resource limitations when scheduling a job

`Cloud Pipeline` supports queueing of the jobs, that cannot be scheduled to the existing or new compute nodes immediately.

Queueing occurs if:

- `cluster.max.size` limit  is reached (configurable via `Preferences`)
- Cloud Provider limitations are reached (e.g. [AWS EC2 Limits](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-resource-limits.html))

If (**1**) happens - job will sit in the `QUEUED` state, until a spare will be available or stopped manually. This is the correct behavior.  
But if (**2**) occurs - an error will be raised by the Cloud Provider, `Cloud Pipeline` treats this as an issue with the node creation. Autoscaler will then resubmit the node creation task for `cluster.nodeup.retry.count` times (default: 5) and then fail the job run.  
This behavior confuses users, as (**2**) shall behave almost in the same manner as (**1**) - job shall be kept in the queue until there will be free space for a new node.

In **`v0.15`** (**2**) scenario works as described:

- If a certain limit is reached (e.g. number of `m4.large` instances exceeds the configured limit) - run will not fail, but will await for a spare node or limit increase
- A warning will be highlighted in the job initialization log:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_Resource_Limits_1.png)

## Allow to terminate paused runs

Some of the jobs, that were paused (either manually, or by the automated service), may be not needed anymore.  
But when a run is paused - the user cannot terminate/stop it before resuming. I.e. one have to run `RESUME` operation, wait for it's completion and then `STOP` the run.

While this is the expected behavior (at least designed in this manner) - it requires some extra steps to be performed, which may look like meaningless (why one shall resume a run, that is going to be stopped?).

Another issue with such a behavior is that in certain "bad" conditions - paused runs are not able to resume and just fail, e.g.:

- An underlying instance is terminated outside of the Cloud Pipeline
- Docker image was removed from the registry
- And other cases that are not yet uncovered

This introduces a number of stale runs, that just sit there in the PAUSED state and nobody can remove them.

To address those concerns - current version of `Cloud Pipeline` allows to terminate `PAUSED` run, without a prior `RESUME`. This operation can be performed by the `OWNER` of the run and the `ADMIN` users.
Termination of the `PAUSED` run drops the underlying cloud instance and marks the run as `STOPPED`.

From the GUI perspective - `TERMINATE` button is shown (instead of `STOP`), when a run is in the `PAUSED` state:  

- on the "Active runs" page  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_TerminateButton_1.png)
- on the "Run logs" page  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_TerminateButton_2.png)
- on the "Dashboard" page  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_TerminateButton_3.png)

Clicking it - performs the run termination, as described above.  
See more information [here](../../manual/11_Manage_Runs/11.1._Manage_runs_lifecycles.md#stopterminate-run).

## Pre/Post-commit hooks implementation

In certain use-cases, extra steps shall be executed before/after running the commit command in the container.  
E.g. imagine the following scenario:

1. User launches `RStudio`
2. User installs packages and commits it as a new image
3. User launches the new image
4. The following error will be displayed in the R Console:

```r
16 Jan 2019 21:17:15 [rsession-GRODE01] ERROR session hadabend; LOGGED FROM: rstudio::core::Error {anonymous}::rInit(const rstudio::r::session::RInitInfo&) /home/ubuntu/rstudio/src/cpp/session/SessionMain.cpp:563
```

There is nothing bad about this message and states that previous RSession was terminated in a non-graceful manner.
RStudio will work correctly, but it may confuse the users.

While this is only one example - there are other applications, that require extra cleanup to be performed before the termination.

To workaround such issues (RStudio and others) an approach of `pre/post-commit hooks` is implemented. That allows to perform some graceful cleanup/restore before/after performing the commit itself.

Those hooks are valid only for the specific images and therefore shall be contained within those images. `Cloud Pipeline` itself performs the calls to the hooks if they exist.

Two preferences are introduced:

- `commit.pre.command.path`: specified a path to a script within a docker image, that will be executed in a currently running container, **BEFORE** `docker commit` occurs (default: `/root/pre_commit.sh`).
    - This option is useful if any operations shall be performed with the running processes (e.g. send a signal), because in the subsequent `post` operation - only filesystem operations will be available.
    - **_Note_** that any changes done at this stage will affect the running container.
- `commit.post.command.path`: specified a path to a script within a docker image, that will be executed in a committed image, **AFTER** `docker commit` occurs (default: `/root/post_commit.sh`).
    - This hook can be used to perform any filesystem cleanup or other operations, that shall not affect the currently running processes.
- If a corresponding pre/post script is not found in the docker image - it will not be executed.

For more details see [here](../../manual/10_Manage_Tools/10.4._Edit_a_Tool.md#committing-features).

## Restricting manual installation of the nvidia tools

It was uncovered that some of the GPU-enabled runs are not able to initialize due to an issue describe at [NVIDIA/nvidia-docker#825](https://github.com/NVIDIA/nvidia-docker/issues/825).

To limit a possibility of producing such docker images (which will not be able to start using GPU instance types) - a set of restrictions/notifications was implemented:

- A notification is now displayed (in the Web GUI), that warns a user about the risks of installing any of the `nvidia` packages manually. And that all `cuda-based` dockers shall be built using `nvidia/cuda` base images instead:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_GPU_Notification_1.png)
- Restrict users (to the reasonable extent) from installing those packages while running SSH/terminal session in the container. If user will try to install a `restricted` package - a warning will be shown (with an option to bypass it - for the advanced users):  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_GPU_Notification_2.png)

## Setup swap files for the Cloud VMs

This feature is addresses the same issues as the previous **Notifications about high resources pressure** by making the compute-intensive jobs runs more reliable.

In certain cases jobs may fail with unexpected errors if the compute node runs `Out Of Memory`.

**`v0.15`** provides an ability for admin users to configure a default `swap` file to the compute node being created.
This allow to avoid runs failures due to memory limits.

The size and the location of the `swap` can be configured via `cluster.networks.config` item of the `Preferences`. It is accomplished by adding the similar `json` object to the platform's global or a region/cloud specific configuration:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_SwapFiles_1.png)

Options that can be used to configure `swap`:

- `swap_ratio` - defines a swap file size. It is equal the node RAM multiplied by that ratio. If ratio is 0, a swap file will not be created (default: 0)
- `swap_location` - defines a location of the swap file. If that option is not set - default location will be used (default: AWS will use `SSD/gp2` EBS, Azure will be [Temporary Storage](https://blogs.msdn.microsoft.com/mast/2013/12/06/understanding-the-temporary-drive-on-windows-azure-virtual-machines/))

See an example [here](../../manual/12_Manage_Settings/12.11._Advanced_features.md#setup-swap-files-for-the-cloud-vms).

## Run's system paths shall be available for the general user account

Previously, all the system-level directories (e.g. pipeline code location - `$SCRIPTS_DIR`, input/common data folders - `$INPUT_DATA`, etc.) were owned by the `root` user with `read-only` access to the general users.

This was working fine for the `pipeline` runs, as they are executed on behalf of `root`. But for the interactive sessions (SSH/Web endpoints/Desktop) - such location were not writable.

From now on - all the system-level location will be granted `rwx` access for the `OWNER` of the job (the user, who launched that run).

## Renewed WDL visualization

**`v0.15`** offers an updated Web GUI viewer/editor for the WDL scripts. These improvements allow to focus on the WDL diagram and make it more readable and clear. Which very useful for large WDL scripts.

- Auxiliary controls ("Save", "Revert changes", "Layout", "Fit to screen", "Show links", "Zoom out", "Zoom in", "Fullscreen") are moved to the left side of the `WDL GRAPH` into single menu:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_WDLRenewed_1.png)
- WDL search capabilities are added. This feature allows to search for any task/variable/input/output within the script and focus/zoom to the found element. Search box is on the auxiliary controls menu and supports entry navigation (for cases when more than one item was found in the WDL):  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_WDLRenewed_3.png)  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_WDLRenewed_4.png)
- Workflow/Task editor is moved from the modal popup to the right floating menu `PROPERTIES`:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_WDLRenewed_5.png)

See more details [here](../../manual/06_Manage_Pipeline/6.1.1_Building_WDL_pipeline_with_graphical_PipelineBuilder.md).

## "QUEUED" state of the run

Previously, user was not able to distinguish runs that are waiting in the queue and the runs that are being initialized (both were reporting the same state using the same icons).
Now, a more clear run's state is provided - "QUEUED" state is introduced:

![CP_v.0.15_ReleaseNotes](attachments/RN015_QueueStatus_1.png)  

During this phase of the lifecycle - a job is waiting in the queue for the available compute node. Typically this shall last for a couple of second and proceed to the initialization phase. But if this state lasts for a long time - it may mean that a cluster capacity is reached (limited by the administrator).

This feature allows users to make a decision - whether to wait for run in a queue or stop it and resubmit.

See more details [here](../../manual/11_Manage_Runs/11._Manage_Runs.md#active-runs).

## Help tooltips for the run state icons

With the runs' `QUEUED` state introduction - we now have a good number of possible job phases.

To make the phases meaning more clear - tooltips are provided when hovering a run state icon within all relevant forms, i.e.: `Dashboard`, `Runs` (`Active Runs`/`History`/etc.), `Run Log`.  

Tooltips contain a state name in bold (e.g. **Queued**) and a short description of the state and info on the next stage:  

![CP_v.0.15_ReleaseNotes](attachments/RN015_TooltipsStatuses_1.png)

See more details - [Active runs states](../../manual/11_Manage_Runs/11._Manage_Runs.md#active-runs), [Completed runs states](../../manual/11_Manage_Runs/11._Manage_Runs.md#completed-runs) and [Home page](../../manual/18_Home_page/18._Home_page.md#recently-completed-runs).

## VM monitor service

For various reasons cloud VM instances may "hang" and become invisible to `Cloud Pipeline` services. E.g. VM was created but some error occurred during joining k8s cluster or a communication to the Cloud Providers API is interrupted.

In this case `Autoscaler` service will not be able to find such instance and it won't be shut down. This problem may lead to unmonitored useless resource consumption and billing.

To address this issue - a separate `VM monitoring` service was implemented:

- Tracks all VM instances in the registered Cloud Regions
- Determines whether instances is in "hang" state
- Notifies a configurable set of users about a possible problem

Notification recipients (administrators) may check the actual state of VM in Cloud Provider console and shut down VM manually.

## Web GUI caching

Previously, `Cloud Pipeline` Web GUI was not using HTTP caching to optimize the page load time. Each time application was loaded - `~2Mb` of the app bundle was downloaded.

This caused "non-optimal" experience for the end-users.

Now the application bundle is split into chunks, which are identified by the `content-hash` in names:

- If nothing is changed - no data will be downloaded
- If some part of the app is changed - only certain chunks will be downloaded, not the whole bundle

Administrator may control cache period using the `static.resources.cache.sec.period` parameter in the `application.properties` of the Core API service.

## Installation via pipectl

Previous versions of the `Cloud Pipeline` did not offer any automated approach for deploying its components/services.
All the deployment tasks were handed manually or by custom scripts.

To simplify the deployment procedure and improve stability of the deployment - `pipectl` utility was introduced.

`pipectl` offers an automated approach to deploy and configure the `Cloud Pipeline` platform, as well as publish some demo pipelines and docker images for NGS/MD/MnS tasks.

Brief description and example installation commands are available in the [pipectl's home directory](https://github.com/epam/cloud-pipeline/tree/develop/deploy).

More sophisticated documentation on the installation procedure and resulting deployment architecture will be created further.

## Add more logging to troubleshoot unexpected pods failures

When a `Cloud Pipeline` is being for a long time (e.g. years), it is common to observe rare "strange" problems with the jobs execution.
I.e. the following behavior was observed couple of times over the last year:

### _Scenario 1_

1. Run is launched and initialized fine
2. During processing execution - run fails
3. Console logs print nothing, compute node is fine and is attached to the cluster

### _Scenario 2_

1. Run is launched, compute node is up
2. Run fails during initialization
3. Console logs print the similar error message:

```bash
failed to open log file "/var/log/pods/**.log": open /var/log/pods/**.log: no such file or directory
```

Both scenarios are flaky and almost impossible to reproduce. To provide more insights into the situation - an extended `node-level` logging was implemented:

1. `kubelet` logs (from all compute nodes) are now written to the files (via `DaemonSet`)
2. Log files are streamed to the storage, identified by `storage.system.storage.name` preference
3. Administrators can find the corresponding node logs (e.g. by the `hostname` or `ip` that are attached to the run information) in that storage under `logs/node/{hostname}`

See an example [here](../../manual/11_Manage_Runs/11._Manage_Runs.md#console-output).

## Displaying information on the nested runs within a parent log form

Previously, if user launched a run, that has a number of children (e.g. a cluster run or any other case with the parent-id specified), he could view the children list only from "Runs" page.

In **`v.0.15`** a convenient opportunity to view the list of children directly in the parent's run logs form is implemented:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_NestedRunsIcons_1.png)

For each child-run in the list the following information is displayed:

- State icons with help tooltips when hovering over them
- Pipeline name and version/docker image and version
- Run time duration

Similar as a parent-run state, states for nested runs are automatically updated without page refreshing. So, you can watch for them in real time.

If you click any of the children-runs, you will navigate to its log page.

That feature is implemented for the comleted runs too:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_NestedRunsIcons_2.png)

More information about nested runs displaying see [here](../../manual/11_Manage_Runs/11._Manage_Runs.md#active-cluster-runs) and [here](../../manual/11_Manage_Runs/11._Manage_Runs.md#general-information).

## `pipe` CLI warnings on the JWT expiration

By default, when `pipe` CLI is being configured JWT token is given for one month, if user didn't select another expiration date.

In **`v.0.15`** extra `pipe` CLI warnings are introduced to provide users an information on the JWT token expiration:

- When `pipe configure` command is executed - the warning about the expiration date of the provided token is printed, if it is less than 7 days left:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_JWTtokenExp_1.png)
- When `--version` option is specified - `pipe` prints dates of issue and expiration for the currently used token:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_JWTtokenExp_2.png)
- When any other command is running - the warning about the expiration date of the provided JWT token is printed, if it is less than 7 days left:  
![CP_v.0.15_ReleaseNotes](attachments/RN015_JWTtokenExp_3.png)

See more information about `pipe` CLI installation [here](../../manual/14_CLI/14.1._Install_and_setup_CLI.md#how-to-install-and-setup-pipe-cli).

## `pipe` configuration for using NTLM Authentication Proxy

For some special customer needs, `pipe` configuration for using NTLM Authentication Proxy, when running in Linux, could be required.

For that, several new options were added to `pipe configure` command:

- `-nt` or `--proxy-ntlm` - flag that enable NTLM proxy support
- `-nu` or `--proxy-ntlm-user` - username for NTLM proxy authorization
- `-np` or `--proxy-ntlm-pass` - password for NTLM proxy authorization
- `-nd` or `--proxy-ntlm-domain` - domain for NTLM proxy authorization

If `--proxy-ntlm` is set, `pipe` will try to get the proxy value from the environment variables or `--proxy` option (`--proxy` option has a higher priority).  
If `--proxy-ntlm-user` and `--proxy-ntlm-pass` options are not set - user will be prompted for username/password in an interactive manner.  

Valid configuration examples:

- User will be prompted for NTLM Proxy Username, Password and Domain:

```bash
pipe configure --proxy-ntlm
...
Username for the proxy NTLM authentication: user1
Domain of the user1 user: ''
Password of the user1 user:
```

- Use `http://myproxy:3128` as the "original" proxy address. User will not be prompted for NTLM credentials:

```bash
pipe configure --proxy-ntlm --proxy-ntlm-user $MY_NAME --proxy-ntlm-pass $MY_PASS --proxy "http://myproxy:3128"
```

See more information about `pipe` CLI installation and configure [here](../../manual/14_CLI/14.1._Install_and_setup_CLI.md).

## Environment Modules support for the Cloud Pipeline runs

The `Environment Modules` [package](http://modules.sourceforge.net/index.html) provides for the dynamic modification of a user's environment via `modulefiles`.

In current version, an ability to configure the `Modules` support for the compute jobs is introduced, if this is required by any use case.

For using facilities of the `Environment Modules` package, a new system parameter was added to the Cloud Pipeline:

- **`CP_CAP_MODULES`** _(boolean)_ - enables installation and using the `Modules` for the current run (for all supported Linux distributions)

If `CP_CAP_MODULES` system parameter is set - the `Modules` will be installed and made available. While installing, `Modules` will be configured to the source `modulefiles` path from the `CP_CAP_MODULES_FILES_DIR` launch environment variable (value of this variable could be set only by admins via system-level settings). If that variable is not set - default `modulefiles` location will be used.

See an example [here](../../manual/15_Interactive_services/15.2_Using_Terminal_access.md#example-using-of-environment-modules-for-the-cloud-pipeline-runs).

## Sharing SSH access to running instances with other user(s)/group(s)

As was introduced in [Release Notes v.0.13](../v.0.13/v.0.13_-_Release_notes.md#running-instances-sharing-with-other-users-or-groups-of-users), for certain use cases it is beneficial to be able to share applications with other users/groups.  

**`v0.15`** introduces a feature that allows to share SSH-sessions for such shared runs:

1. User can share a run with others:
    - "Share with: ..." parameter, within a run log form, can be used for this  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SharingInstancesSSH_1.png)
    - Specific users or whole groups can be set for sharing  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SharingInstancesSSH_2.png)
    - Once this is set - other users will be able to access run's endpoints
    - Also you can share SSH access to the running instance via setting "**Enable SSH connection**" checkbox  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SharingInstancesSSH_3.png)
2. **SERVICES** widget within a Home dashboard page lists such "shared" services. It displays a "catalog" of services, that can be accessed by a current user, without running own jobs.  
To open shared instance application user should click the service name.  
To get SSH-access to the shared instance user should hover over service "card" and click the **SSH** hyperlink  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_SharingInstancesSSH_4.png)

For more information about runs sharing see [11.3. Sharing with other users or groups of users](../../manual/11_Manage_Runs/11.3._Sharing_with_other_users_or_groups_of_users.md).

## Verification of docker/storage permissions when launching a run

Users are allowed to launch pipeline, detached configuration or tool if they have a corresponding **permission** for that executable.  
But in some cases this verification is not enough, e.g. when user has no read permission for input parameter - in this case, run execution could cause an error.

In **`v0.15`** additional verification implemented that checks if:

- `execution` is allowed for specified docker image;
- `read` operations are allowed for **input** and **common** path parameters;
- `write` operations are allowed for **output** path parameters.

If there are such permission issues, run won't be launched and special warning notifications will be shown to a user, e.g.:  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_PermissionsVerification_1.png)  
    ![CP_v.0.15_ReleaseNotes](attachments/RN015_PermissionsVerification_2.png)

For more details see sections [6.2. Launch a pipeline](../../manual/06_Manage_Pipeline/6.2._Launch_a_pipeline.md), [7.2. Launch Detached Configuration](../../manual/07_Manage_Detached_configuration/7.2._Launch_Detached_Configuration.md) and [10.5. Launch a Tool](../../manual/10_Manage_Tools/10.5._Launch_a_Tool.md).

***

## Notable Bug fixes

### Incorrect behavior of the global search filter

[#221](https://github.com/epam/cloud-pipeline/issues/221)

When user was searching for an entry, that may belong to different classes (e.g. `issues` and `folders`) - user was not able to filter the results by the class

### "COMMITTING..." status hangs

[#152](https://github.com/epam/cloud-pipeline/issues/152)

In certain cases, while committing pipeline with the stop flag enabled - the run's status hangs in `Committing...` state. Run state does not change even after the commit operation succeeds and a job is stopped.

### Instances of Metadata entity aren't correctly sorted

[#150](https://github.com/epam/cloud-pipeline/issues/150)

Metadata entities (i.e. project-related metadata) sorting was faulty:

1. Sort direction indicator (Web GUI) was displaying an inverted direction
2. Entities were not sorted correctly

### Tool group cannot be deleted until all child tools are removed

[#144](https://github.com/epam/cloud-pipeline/issues/144)

If there is a tool group in the registry, which is not empty (i.e. contains 1+ tools) - an attempt to delete it throws SQL error.

It works fine if the child tools are dropped beforehand.

Now it is possible to delete such a group if a `force` flag is set in the confirmation dialog.

### Missing region while estimating a run price

[#93](https://github.com/epam/cloud-pipeline/issues/93)

On the launch page, while calculating a price of the run, Cloud Provider's region was ignored. This way a calculation used a price of the specified instance type in any of the available regions. In practice, requested price may vary from region to region.

### Cannot specify region when an existing object storage is added

[#45](https://github.com/epam/cloud-pipeline/issues/45)

Web GUI interface was not providing an option to select a region when adding an existing object storage. And it was impossible to add a bucket from the non-default region.

### ACL control for PIPELINE_USER and ROLE entities for metadata API

[#265](https://github.com/epam/cloud-pipeline/issues/265)

All authorized users were permitted to browse the metadata of `users` and `roles` entities. But those entries may contain a sensitive data, that shall not be shared across users.

Now a general user may list only personal `user-level` metadata. Administrators may list both `users` and `roles` metadata across all entries.

### Getting logs from Kubernetes may cause `OutOfMemory` error

[#468](https://github.com/epam/cloud-pipeline/issues/468)

For some workloads, container logs may become very large: up to several gigabytes. When we tried to fetch such logs it is likely to cause `OutOfMemory` error, since Kubernetes library tries to load it into a single String object.  
In current version, a new system preference was introduced: **`system.logs.line.limit`**. That preference sets allowable log size in lines. If actual pod logs exceeds the specified limit only log tail lines will be loaded, the rest will be truncated.

### Layout is broken for pipeline's versions table

[#553](https://github.com/epam/cloud-pipeline/issues/553)

Previously, **pipeline versions page** had broken layout if there were pipeline versions with long description.

### AWS: Incorrect `nodeup` handling of spot request status

[#556](https://github.com/epam/cloud-pipeline/issues/556)

Previously, in a situation when an `AWS` spot instance created after some timeout - spot status wasn't updated correctly in the handling of `spot request status`. It might cause errors while getting spot instance info.

### Not handling clusters in `autopause` daemon

[#557](https://github.com/epam/cloud-pipeline/issues/557)

Previously, if cluster run was launched with enabled "Auto pause" option, parent-run or its child-runs could be paused (when autopause conditions were satisfied, of course). It was incorrect behavior because in that case, user couldn't resume such paused runs and go on his work (only "Terminate" buttons were available).
In current version, `autopause` daemon doesn't handle any clusters ("Static" or "Autoscaled").  
Also now, if the cluster is configured - **Auto pause** checkbox doesn't display in the **Launch Form** for the `On-Demand` node types.

### Incorrect `pipe` CLI version displaying

[#561](https://github.com/epam/cloud-pipeline/issues/561)

Previously, `pipe` CLI version displayed incorrectly for the `pipe` CLI installations performed via hints from the **Cloud Pipeline** System Settings menu.
