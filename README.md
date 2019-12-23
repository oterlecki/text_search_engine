#### **1. How to run on Mac OS**

- You need installed Java, Scala and SBT.

        brew install java
        brew install scala
        brew install sbt

- And download project:

        git clone https://github.com/oterlecki/text_search_engine.git

#### **2. Usage**

- Enter the project: 

        cd _path_to_project_project_/text_search_engine

- To run type: 

        console> sbt 
        console> ~run _path_to_your_directory_with_test_files_
        
- If directory was provided and `>search` bar occurs type words separated by space to search inside text files, f.e:

       search>cat dog
       
- The results of scoring should be displayed. To exit search mode just type `:quit`.

#### **3. Proposed improvements**

- Create `resources` in `test` directory to properly test `readFile`, `index` and `indexFile` function from `Program` class

- Mock `System.in` and `System.out` and create tests for `iterate` function from `Program` class

- Implement better word scoring algorithm (f.e create and take into account each word weight or number of occurrences) 

- Create separeted unit tests for Ranking and Index classes
