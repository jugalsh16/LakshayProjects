var items;

// try to load from localStorage
try {
  items = JSON.parse( localStorage.todoItems );
} catch ( err ) {}

if ( !items ) {
  items = [
    { completed: true,  description: 'Add a todo' },
    { completed: false, description: 'Add some more todos' },
    { completed: false, description: 'Build something with Ractive.js' }
  ]
}

var ractive = new TodoList({
  el: demo,
  data: {
    items: items
  }
});

// persist changes to localStorage if possible
ractive.observe( 'items', function ( items ) {
  try {
    localStorage.todoItems = JSON.stringify( items );
  } catch ( err ) {}
});