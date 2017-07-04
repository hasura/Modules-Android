import Vapor 

let drop = Droplet()

drop.get("permission") { request in
    
//    let fileID = request.query["file_id"]?.string
//    let fileOP = request.query["file_op"]?.string
    return try JSON(node: [
        ["value":"random"]
//        "file_id": fileID!,
//        "file_op": fileOP!
        ])
    
}

drop.run()
