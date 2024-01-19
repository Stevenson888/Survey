var debounceTimer = null;

// export default function debounce(callback, duration) {
//   return function (...args) {
//
//     console.log("===debounce===")
//
//     let ctx = this;
//     if (debounceTimer) clearTimeout(debounceTimer);
//     debounceTimer = setTimeout(() => {
//       console.log('定时器函数执行了')
//       callback.apply(ctx, args);
//     }, duration);
//   };
// };

// export default function debounce(callback, duration, isFirstExecution) {
//   return function (...args) {
//     let ctx = this;
//     const delay = function () {
//       debounceTimer = null;
//
//       console.log("===debounceFirst===")
//
//       console.log('定时器函数执行了')
//       if (!isFirstExecution) callback.apply(ctx, args);
//     };
//     let executeNow = isFirstExecution && !debounceTimer;
//     clearTimeout(debounceTimer);
//     debounceTimer = setTimeout(delay, duration);
//     if (executeNow) callback.apply(ctx, args);
//   };
// };
// isFirstExecution为true时只执行第一次，为false时只执行最后一次


// 防抖的函数
export default function debounce(func, wait, immediate){
var timeout,result;
  let decounced = function () {
    var context = this;
    var args = arguments;
    if (timeout) clearTimeout(timeout);
    if (immediate) {
      var callNow = !timeout ;
      timeout = setTimeout(function () {
        timeout = null;
      }, wait);
  // 立即执行
      if (callNow) result = func.apply(context, args)
    } else {
  //不会立即执行
      timeout = setTimeout(function (){
        func.apply(context, args);
      },wait);
    }
    return result;
  }
}
