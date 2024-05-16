// module.exports = {
//     devServer: {
//         hot: true,
//         open: true,
        // proxy: {
        //     '/api': {
        //         target: 'http://localhost:8080',
        //         changeOrigin: true,
        //         pathRewrite: {
        //             '^/api': ''
        //         }
        //     }
        // },
        disableHostCheck: true
//     }
// };

module.exports = {
    devServer: {
      host: '0.0.0.0',
      open: true,
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:8080',  
          changeOrigin: true,  
          pathRewrite: {
            '^/api': '' 
          }
        }
      },
      disableHostCheck: true
    }
  };