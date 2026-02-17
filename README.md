# AADL to Pi-Calculus Model Transformation Tool

This repository contains a model-to-text (M2T) transformation engine designed to bridge the gap between architectural modeling and formal verification. The tool automatically generates **Pi-Calculus** formalisms from **AADL (Architecture Analysis & Design Language)** models.

---

## Project Architecture

The repository is structured as a multi-plugin Eclipse project to allow for modular development of the generator logic and future user interface components.

```text
aadl-to-pi-calculus/
├── plugins/
│   └── AADLtoPiGenerator/                      # Core Acceleo transformation logic
├── models/                                     # Sample AADL models (.aadl and .aaxl2)                                   
└── README.md
