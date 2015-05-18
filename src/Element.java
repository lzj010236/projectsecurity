public class Element {
	String original_job;
	String current_job;
	int original_job_position;
	int current_position;
	int current_character;
	char[] character_array;
	int depth;
	int maximum_depth;

	Element(String original_job, int original_job_position,
			char[] character_array, int depth, int k) {
		this.character_array = character_array;
		this.original_job = original_job;
		current_position = original_job_position + 1;
		current_character = 0;
		current_job = str_insert(original_job, current_position, 0);
		this.original_job_position = original_job_position;
		this.depth = depth;
		this.maximum_depth = k;
	}

	private String str_insert(String ori_word, int current_position, int i) {
		return ori_word.substring(0, current_position)
				+ String.valueOf(character_array[i])
				+ ori_word.substring(current_position, ori_word.length());
	}

	public boolean GenerateSibling() {
		this.current_character++;
		if (current_character >= character_array.length) {
			current_position += 1;
			current_character = 0;
		}
		if (current_position >= 1 + original_job.length()) {
			return false;
		} else {
			current_job = str_insert(original_job, current_position,
					current_character);
			return true;
		}
	}

	public Element GenerateFirstSon() {
		if (original_job_position >= original_job.length() + 1)
			return null;
		else if (depth < maximum_depth) {
			return new Element(str_insert(original_job, current_position,
					current_character), current_position, character_array,
					depth + 1, this.maximum_depth);

		} else
			return null;
	}
}