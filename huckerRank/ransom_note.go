package main

func RansomeNote(magazine []string, note []string) string {
	hashMap := make(map[string]struct{}, len(magazine))

	for i := range magazine {
		hashMap[magazine[i]] = struct{}{}
	}

	for i := range note {
		_, exist := hashMap[note[i]]
		if !exist {
			return "NO"
		}
	}

	return "YES"

}
