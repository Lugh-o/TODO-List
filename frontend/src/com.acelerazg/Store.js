export const globals = {
	selectedFilter: null,
	editingTask: null,
	colors: {
		AliceBlue: "#f0f6fa",
		ColumbiaBlue1: "#cee5f2",
		ColumbiaBlue2: "#accbe1",
		AirSuperiorityBlue: "#7c98b3",
		PainesGray1: "#637081",
		PainesGray2: "#536b78",
		Onyx: "#303538",
	
		Priority5: "#2e7d32",
		Priority4: "#9e9d24",
		Priority3: "#ffb300",
		Priority2: "#f4511e",
		Priority1: "#c62828",
	}
}

export const TaskService = {
	list: [],
	currentId: 1,

	constructor(tasks) {
		this.list = tasks;
		this.currentId = tasks.length + 1;
	},

	create(data) {
		const task = {
			id: this.currentId,
			name: data.name,
			description: data.description,
			endDate: data.endDate,
			priority: data.priority,
			category: data.category,
			status: data.status.toUpperCase(),
		};
		this.list.push(task);
		this.currentId++;

		return task;
	},

	update(task, data) {
		task.name = data.name;
		task.description = data.description;
		task.endDate = data.endDate;
		task.priority = data.priority;
		task.category = data.category;
		task.status = data.status.toUpperCase();
	},

	delete(task) {
		const index = this.list.indexOf(task);
		if (index !== -1) this.list.splice(index, 1);
	},
};
