# N-gram and Markov Model Text Analysis

A Java console program that analyzes a text file with n-grams. It lists the most frequent words and builds 2-gram and 3-gram Markov models from the text. Written as a course project in Natural Language Processing.

## How it works

1. **Cleaning:** keeps only letters and whitespace, lowercases the text, and joins it into a single line.
2. **1-gram analysis:** counts each word and prints the 50 most frequent, with their probabilities (`count / total words`).
3. **2-gram and 3-gram Markov models:** counts which word follows each one-word or two-word prefix, then prints the 50 most probable transitions (`count / total occurrences of the prefix`).
4. **Timing:** prints the total run time in milliseconds.

## Running it

You need JDK 8 or newer. From the repository root:

```bash
javac -encoding UTF-8 -d out src/nGramAnalysis.java
java -cp out nGramAnalysis
```

When prompted, enter the path of a text file, such as `samples/grimms-fairy-tales_P1.txt`. Input files must be UTF-8 encoded.

## Example result

Most frequent words in `samples/grimms-fairy-tales_P1.txt`, in a 264 ms run:

| Word | Count | Probability |
|------|-------|-------------|
| the | 747 | 0.0726 |
| and | 546 | 0.0531 |
| to | 271 | 0.0264 |
| a | 244 | 0.0237 |
| he | 233 | 0.0227 |

The full algorithm description and test results are in the [project report](docs/ngram-algorithm-report.pdf).

## Notes

- The Turkish results in the report have some letters missing (`için` appears as `iin`). Those test files weren't UTF-8, so the program dropped the Turkish characters while reading them.
- The Turkish books used for testing in the report are not included because of copyright. The repository only includes the Grimm sample.
