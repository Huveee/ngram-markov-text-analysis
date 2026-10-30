import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class nGramAnalysis {
    public static void main(String[] args) throws IOException {
    	
        // Getting the start time
        long startTime = System.currentTimeMillis();
        
        Scanner scanner = new Scanner(System.in);

        // Request file name
        System.out.print("Please enter the file name: ");
        String filePath = scanner.nextLine(); 
        String text = readFile(filePath);

        // Cleaning process: Leave only letters and residue
        StringBuilder cleanedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c) || Character.isWhitespace(c)) {
                cleanedText.append(Character.toLowerCase(c));
            }
        }

        String cleaned = cleanedText.toString()
                .replace("\r", " ") //  clear `\r` characters
                .replace("\n", " ") // clear `\n` characters
                .trim(); // Clear leading and trailing spaces


        // 1-gram analysis
        System.out.println("\n1-GRAM ANALYSIS:\n");
        unigramAnalysis(cleaned); // Use cleaned text

        // 2-gram and 3-gram analysis available for Markov model
        System.out.println("\n2-GRAM MARKOV MODEL:\n");
        buildMarkovModel(cleaned, 1); // Use cleaned text

        System.out.println("\n3-GRAM MARKOV MODEL:\n");
        buildMarkovModel(cleaned, 2); // Use cleaned text
        
        // Getting the finish time
        long endTime = System.currentTimeMillis();
        
        // Calculating the total time
        long totalTime = endTime - startTime;

        System.out.println("\nTotal Run Time: " + totalTime + " millisecond");
    }
    

    // Read text from file
    private static String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
        }
        return sb.toString();
    }

    
    // 1-Gram Analysis: Calculate the frequency of words
    private static void unigramAnalysis(String text) {
        String[] words = text.split("\\s+");
        Map<String, Integer> wordCounts = new HashMap<>();

        // Calculate word frequencies
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // Find total word count
        int totalWords = Arrays.stream(words).mapToInt(w -> 1).sum();

        // Print word frequencies and probabilities
        System.out.println("Word Frequencies and Probabilities (Top 50):");
        wordCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Frekansa göre azalan sırada
                .limit(50) // İlk 50'yi seç
                .forEach(entry -> {
                    String word = entry.getKey();
                    int count = entry.getValue();
                    double probability = (double) count / totalWords;
                    System.out.printf("%s: %d (%.4f)%n", word, count, probability);
                });
    }

    
    // N-Gram Analysis for Markov model
    private static void buildMarkovModel(String text, int n) {
        String[] words = text.split("\\s+");
        Map<String, Map<String, Integer>> markovModel = new HashMap<>();

        // N-gram creation
        for (int i = 0; i <= words.length - n - 1; i++) {
            String prefix = String.join(" ", Arrays.copyOfRange(words, i, i + n)); // Prefix 
            String nextWord = words[i + n]; // Next word

            markovModel.putIfAbsent(prefix, new HashMap<>());
            Map<String, Integer> nextWordCounts = markovModel.get(prefix);

            // Increase crossover frequency
            nextWordCounts.put(nextWord, nextWordCounts.getOrDefault(nextWord, 0) + 1);
        }

        // Probability calculations and ranking
        List<Map.Entry<String, Double>> probabilities = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : markovModel.entrySet()) {
            String prefix = entry.getKey();
            Map<String, Integer> nextWords = entry.getValue();
            int total = nextWords.values().stream().mapToInt(Integer::intValue).sum();

            for (Map.Entry<String, Integer> wordEntry : nextWords.entrySet()) {
                String word = wordEntry.getKey();
                int count = wordEntry.getValue();
                double probability = (double) count / total;

                probabilities.add(new AbstractMap.SimpleEntry<>(
                        prefix + " -> " + word, probability
                ));
            }
        }

        // Sort the possibilities in descending order
        probabilities = probabilities.stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());

        // Print first 50 results
        System.out.println("\n50 Highest Probabilities:");
        for (int i = 0; i < Math.min(50, probabilities.size()); i++) {
            Map.Entry<String, Double> entry = probabilities.get(i);
            System.out.printf("%d. %s: %.4f%n", i + 1, entry.getKey(), entry.getValue());
        }
    }
}
