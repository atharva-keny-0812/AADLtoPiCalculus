# AADL to $\pi$-Calculus Model Transformation Tool

This repository provides an automated model-driven toolchain to bridge the gap between architectural modeling in **AADL** and formal verification in **$\pi$-Calculus**. The tool automates the mapping rules defined in our approach to facilitate the formal analysis of real-time systems.

---

## Repository Structure

* **`plugins/`**: Contains the pre-compiled `.jar` files for immediate deployment into OSATE.
    * `org.telecomparis.aadl2pi.jar` (Core logic)
    * `org.telecomparis.aadl2pi.ui.jar` (UI/Menu integration)
* **`src/`**: Contains the Eclipse/OSATE projects for developers to import and build:
    * `org.telecom.acceleo.pi`: Core Acceleo transformation project (.mtl templates).
    * `org.telecomparis.aadl2pi`: Plugin wrapper for the transformation logic.
    * `org.telecomparis.aadl2pi.ui`: UI integration for the OSATE context menu.
* **`sampleAADLProjects/`**: A set of sample AADL projects (including `.aadl` and `.aaxl2` files) used for benchmarking and validation.

---

## Installation & Usage

### For End Users (Immediate Use)
If you simply want to perform transformations within OSATE, you do not need to build the source code.

1.  Navigate to the `plugins/` folder in this repository.
2.  Download the JAR files.
3.  Copy these files into the `plugins/` directory of your **OSATE installation**.
4.  Restart OSATE with the `-clean` flag to refresh the configuration.
5.  **Right-click** any **AADL Instance file (`.aaxl2`)** and select **"Convert AADL to Pi-Calculus"**.

### For Developers (Building from Source)
To explore or modify the mapping rules:

1.  **Import Projects**: Import all projects from the `source_code/` folder into your OSATE workspace.
2.  **Prerequisites**: Ensure you have the **Acceleo** engine installed in your environment.
3.  **Edit Mapping**: Open `generate.mtl` in the `org.telecom.acceleo.pi` project to edit the transformation rules.
4.  **Re-build the Logic**: After changing the `.mtl` file, you must update the plugin:
    * Export the `org.telecom.acceleo.pi` project as a **JAR**.
    * Replace the old JAR in the `lib/` folder of the `org.telecomparis.aadl2pi` project.
5.  **Sync Distribution**: To make your changes available to others, you must update the repository's top-level **`plugins/`** folder:
    * Export the `org.telecomparis.aadl2pi` projects as **Deployable Plug-ins**.
    * Copy the newly generated JARs into the repository's **`plugins/`** folder.
6.  **Test**: Launch a **Runtime Instance** of Eclipse to test your changes live.

---

## Validation with Benchmarks
The `sampleAADLProjects/` folder includes a variety of AADL models. These models serve as a benchmark to ensure that the generated $\pi$-calculus specifications accurately represent the source architecture.

---
