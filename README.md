# SI-Project-Grades-RMP

Very barebones project to analyze UBCGrades and Ratemyprofessors.com

Divided into 5 main steps:
1. Grabbing all the sections from UBCGrades API V2
2. Grabbing all professors at UBC from ratemyprofessors.com
3. Matching names from both sites and filtering until all remaining sections are associated with a professor's tid from ratemyprofessors.com
4. Merging all sections of each professor to obtain each professor's overall grades statistics.
5. Grabbing the "would take again" rating for each professor from ratemyprofessors.com and matching to the professor's grades.

Each step creates a .csv file. Subsequent steps use files from previous steps. One can comment out some steps and run each step individually, modifying the files in between manually for things like properly formatting names, which we didn't have time for in our project due to the sheer number of names.
